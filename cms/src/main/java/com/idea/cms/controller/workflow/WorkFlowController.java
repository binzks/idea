package com.idea.cms.controller.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idea.cms.controller.BaseController;
import com.idea.cms.support.ModulePermission;
import com.idea.cms.support.UserSession;
import com.idea.cms.support.WorkFlow;
import com.idea.cms.support.WorkFlowStep;
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.view.View;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
import com.idea.framework.jdbc.support.model.Order;
import com.idea.framework.jdbc.support.model.OrderType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/workflow")
public class WorkFlowController extends BaseController {

    private Logger logger = Logger.getLogger(WorkFlowController.class);

    /***
     * 工作流列表页面
     *
     * @param mid     模块id
     * @param page    当前页号
     * @param model   页面model
     * @param request 页面请求
     * @return list页面
     */
    @RequestMapping(value = {"/list{mid}{page}", "/list{mid}-{page}"})
    public String getList(@PathVariable String mid, @PathVariable Integer page, Model model, HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            JdbcModel jdbcModel = modulePermission.getJdbcModel();
            WorkFlow workFlow = modulePermission.getWorkFlow();
            // 获取页面查询条件
            List<Filter> filters = initFilters(model, request, view.getColumns(), jdbcModel.getColumns(), modulePermission.getRowFilters());
            int totalCount = jdbcModel.getTotalCount(filters);
            // 初始化页面分页信息
            Integer start = initPages(model, request, view, page, totalCount);
            // 获取数据
            List<Map<String, Object>> dataList = modulePermission.getJdbcModel().selectMaps(filters, start, view.getRowSize());
            model.addAttribute("mid", mid);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("pk", modulePermission.getPkName());
            model.addAttribute("powerActions", modulePermission.getActions());
            model.addAttribute("actions", view.getActions());
            model.addAttribute("powerColumns", modulePermission.getColumns());
            model.addAttribute("columns", view.getColumns());
            model.addAttribute("optWidth", 140);
            model.addAttribute("dataList", dataList);
            model.addAttribute("field", workFlow.getField());
            model.addAttribute("step", workFlow.getStepMap());
            return "/system/workflow/list";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    /***
     * 选择审核人
     *
     * @param mid     模块id
     * @param id      工作流id
     * @param model   页面model
     * @param request 页面请求
     * @return 选择审核人页面
     */
    @RequestMapping(value = "/verify_users{mid}-{id}")
    public String verifyUsers(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter(modulePermission.getPkName(), FilterType.Eq, id));
            Map<String, Object> map = modulePermission.getJdbcModel().selectMap(filters);
            model.addAttribute("mid", mid);
            model.addAttribute("id", id);
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("name", map.get("name"));
            return "system/workflow/verifyUsers";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    /**
     * 保存审核人
     *
     * @param mid     模块id
     * @param id      工作流id
     * @param model   页面model
     * @param request 页面请求
     * @return 返回到工作流页面
     */
    @RequestMapping(value = "/save_verify_users{mid}-{id}")
    public String saveVerifyUsers(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
        try {
            String idValChecked = request.getParameter("idValChecked");
            String nameValChecked = request.getParameter("nameValChecked");
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("user_ids", idValChecked);
            map.put("user_names", nameValChecked);
            JdbcModelCache.getInstance().get("model_sys_workflow_step").update(map);
            return "redirect:" + "/tpl/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    /***
     * 获取可以选择的审核人树
     *
     * @param request  页面请求
     * @param response 审核人树json数据
     */
    @RequestMapping(value = "/getTree.do")
    public void getWorkflowTree(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("id", FilterType.Eq, id));
        Map<String, Object> stepMap = JdbcModelCache.getInstance().get("model_sys_workflow_step").selectMap(filters);
        String userids = (String) stepMap.get("user_ids");
        filters.clear();
        filters.add(new Filter("status", FilterType.Eq, 1));
        List<Map<String, Object>> userMaps = JdbcModelCache.getInstance().get("model_sys_user").selectMaps(filters);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> map : userMaps) {
            String userId = map.get("id").toString();
            if (StringUtils.contains(userids, "," + userId + ",")) {
                list.add(getTreeNode(userId, null, map.get("name").toString(), true));
            } else {
                list.add(getTreeNode(userId, null, map.get("name").toString(), false));
            }
        }
        Gson gson = new Gson();
        writeResponse(response, gson.toJson(list));
    }

    /**
     * 审核数据，先记录审核日志，再审核数据
     *
     * @param userId           审核用户id
     * @param moduleId         模块id
     * @param dataId           审核数据id
     * @param result           审核结果 0-拒绝 1-同意
     * @param describe         描述，填写拒绝原因
     * @param nextValue        数据下一个状态值
     * @param modulePermission 模块权限
     */
    private void verifyData(String userId, String moduleId, String dataId, Integer result, String describe, String nextValue, ModulePermission modulePermission) {
        WorkFlow workFlow = modulePermission.getWorkFlow();
        //生成日志数据
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("workflow_id", workFlow.getId());
        logMap.put("module_id", moduleId);
        logMap.put("data_id", dataId);
        logMap.put("user_id", userId);
        logMap.put("result", result);
        logMap.put("create_time", System.currentTimeMillis() / 1000);
        logMap.put("describe", describe);
        //生成修改数据
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(modulePermission.getPkName(), dataId);
        dataMap.put(workFlow.getField(), nextValue);
        dataMap.put(workFlow.getUserField(), userId);
        dataMap.put(workFlow.getTimeField(), System.currentTimeMillis() / 1000);
        // 先保存日志，再保存数据
        JdbcModelCache.getInstance().get("model_sys_workflow_log").insert(logMap);
        modulePermission.getJdbcModel().update(dataMap);
    }

    @RequestMapping(value = "/agree{mid}-{id}")
    public String agree(@PathVariable String mid, @PathVariable String id, Model model,
                        HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            UserSession userSession = (UserSession) request.getSession().getAttribute("session_user");
            String userId = userSession.getId();
            JdbcModel jdbcModel = modulePermission.getJdbcModel();
            WorkFlow workFlow = modulePermission.getWorkFlow();
            // 获取当前数据所在的节点，不从客户端页面获取，防止并发错误
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("id", FilterType.Eq, id));
            Map<String, Object> data = jdbcModel.selectMap(filters);
            String filed = workFlow.getField();  //工作流状态字段名称
            String currentValue = String.valueOf(data.get(filed)); //当前数据的流程节点值
            WorkFlowStep workFlowStep = workFlow.getWorkFlowStep(currentValue);  //获取工作流节点
            if (null == workFlowStep) {
                throw new Exception("您没有审核权限，请联系管理员!");
            }
            verifyData(userId, mid, id, 1, "", workFlowStep.getNextValue(), modulePermission);
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/refuse.do")
    public String refuse(Model model, HttpServletRequest request) {
        try {
            String mid = request.getParameter("mid");
            String id = request.getParameter("id");
            String describe = request.getParameter("describe");
            ModulePermission modulePermission = getModulePermission(mid, request);
            UserSession userSession = (UserSession) request.getSession().getAttribute("session_user");
            String userId = userSession.getId();
            String nextValue = null;
            for (WorkFlowStep workFlowStep : modulePermission.getWorkFlow().getStepMap().values()) {
                if (workFlowStep.getType() == 0) {
                    nextValue = workFlowStep.getValue();
                    break;
                }
            }
            if (StringUtils.isNotBlank(mid) && StringUtils.isNotBlank(id) && null != nextValue) {
                verifyData(userId, mid, id, 0, describe, nextValue, modulePermission);
            }
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/flow_log.do")
    public void flowLog(Model model, HttpServletRequest request,
                        HttpServletResponse response) {
        String mid = request.getParameter("mid");
        String id = request.getParameter("id");
        ModulePermission modulePermission = getModulePermission(mid, request);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("workflow_id", FilterType.Eq, modulePermission.getWorkFlow().getId()));
        filters.add(new Filter("module_id", FilterType.Eq, mid));
        filters.add(new Filter("data_id", FilterType.Eq, id));
        List<Map<String, Object>> list = JdbcModelCache.getInstance().get("model_show_workflow_log").selectMaps(filters);
        writeResponse(response, new Gson().toJson(list));
    }

}
