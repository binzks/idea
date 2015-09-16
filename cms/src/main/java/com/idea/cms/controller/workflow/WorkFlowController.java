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
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.view.View;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
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

    @RequestMapping(value = {"/list{mid}{page}", "/list{mid}-{page}"})
    public String getList(@PathVariable String mid, @PathVariable Integer page, Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
//			ModulePermission modulePermission = getModulePermission(mid, request);
//			View view = modulePermission.getView();
//			// 获取页面查询条件
//			List<Filter> filters = initFilters(model, request, view, modulePermission.getRowFilters());
//			// 初始化页面分页信息
//			Integer start = initPages(model, request, view, page, filters);
//			// 获取数据
//			List<Map<String, Object>> dataList = view.selectMaps(filters, start, view.getRowSize());
//			model.addAttribute("mid", mid);
//			model.addAttribute("title", view.getTitle());
//			model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
//			model.addAttribute("pk", view.getPkName());
//			model.addAttribute("powerActions", modulePermission.getActions());
//			model.addAttribute("actions", view.getActions());
//			model.addAttribute("powerColumns", modulePermission.getColumns());
//			model.addAttribute("columns", view.getColumns());
//			model.addAttribute("dataList", dataList);
            return "/system/workflow/list";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    @RequestMapping(value = "/verify_users{mid}-{id}")
    public String action(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
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
            return "/system/error";
        }
    }

    @RequestMapping(value = "/save_verify_users{mid}-{id}")
    public String savePower(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
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
            return "/system/error";
        }
    }

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


    private void saveLog(String workFlowCode, Object moduleId, Object dataId, String userId, Object result,
                         String describe) {
        Map<String, Object> logMap = new HashMap<>();
        try {
            logMap.put("workflow_code", workFlowCode);
            logMap.put("module_id", moduleId);
            logMap.put("data_id", dataId);
            logMap.put("user_id", userId);
            logMap.put("result", result);
            logMap.put("create_time", System.currentTimeMillis() / 1000);
            logMap.put("describe", describe);
            JdbcModelCache.getInstance().get("model_sys_workflow_log").insert(logMap);
        } catch (Exception e) {
            logger.error("保存工作流日志失败[" + new Gson().toJson(logMap) + "][" + e + "]");
        }
    }

    @RequestMapping(value = "/agree{mid}-{id}-{value}")
    public String verify(@PathVariable String mid, @PathVariable String id, @PathVariable String value, Model model,
                         HttpServletRequest request, HttpServletResponse response) {
        try {
//            ModulePermission modulePermission = getModulePermission(mid, request);
//            View view = modulePermission.getView();
//            String fieldName = modulePermission.getWorkFlowField();
//            String userField = modulePermission.getWorkFlowUserField();
//            String timeField = modulePermission.getWorkFlowTimeField();
//            // 根据流程节点值，获取下一个流程节点
//            List<Filter> filters = new ArrayList<>();
//            filters.clear();
//            filters.add(new Filter("1", "value", FilterType.Eq, value));
//            Map<String, Object> nextFlow = Think.getModel("model_next_workflow_step").selectMap(filters);
//            // 如果没有下一个节点，认为流程结束，否则进入下一步流程
//            if (null != nextFlow) {
//                UserSession userSession = (UserSession) request.getSession().getAttribute("session_user");
//                Map<String, Object> dataMap = new HashMap<>();
//                dataMap.put(userField, userSession.getId());
//                dataMap.put(timeField, System.currentTimeMillis() / 1000);
//                dataMap.put(fieldName, nextFlow.get("value"));
//                saveLog(view.getWorkflow(), mid, id, userSession.getId(), 1, "");
//                view.update(dataMap);
//            }
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    @RequestMapping(value = "/flowlog{mid}-{id}")
    public String flowlog(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            // ModulePermission modulePermission = getModulePermission(mid,
            // request);
            // View view = modulePermission.getView();
            // String workflow = view.getWorkflow();
            // List<Filter> filters = new ArrayList<>();
            // filters.add(new Filter("workflowid", "=", view.getWorkflow()));
            // Map<String, Object> flow =
            // Think.getModel("model_sys_workflow_step").selectMap();
            return "redirect:" + this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/list" + mid
                    + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

}
