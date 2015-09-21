package com.idea.cms.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.idea.cms.controller.BaseController;
import com.idea.cms.support.ModulePermission;
import com.idea.cms.support.UserSession;
import com.idea.cms.support.WorkFlow;
import com.idea.cms.support.WorkFlowStep;
import com.idea.cms.util.LogUtils;
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.cache.ViewCache;
import com.idea.common.view.View;
import com.idea.framework.Utils.EncryptUtils;
import com.idea.framework.Utils.IpUtils;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
import com.idea.framework.jdbc.support.model.Order;
import com.idea.framework.jdbc.support.model.OrderType;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/sys")
public class LoginController extends BaseController {

    @RequestMapping(value = "/index.do")
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/init.do")
    public String init() {
        //	CmsConfig.init();
        return "redirect:/sys/index.do";
    }

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public String login(Model model, HttpServletRequest request) {
        String code = request.getParameter("loginCode");
        String password = request.getParameter("loginPsw");
        HttpSession session = request.getSession();
        try {
            List<Filter> filterList = new ArrayList<>();
            filterList.add(new Filter("code", FilterType.Eq, code));
            JdbcModel userModel = JdbcModelCache.getInstance().get("model_login_user");
            List<Map<String, Object>> userMaps = userModel.selectMaps(filterList);
            if (userMaps.size() == 0) {
                model.addAttribute("errMsg", "用户不存在");
                return "login";
            }
            // 密码MD5后再MD5
            Map<String, Object> userMap = userMaps.get(0);
            if (!userMap.get("password").equals(EncryptUtils.md5(EncryptUtils.md5(password)))) {
                model.addAttribute("errMsg", "密码错误");
                return "login";
            }
            // 用户模块
            String mIds;
            Object moduleIds = userMap.get("module_ids");
            if (null == moduleIds || moduleIds.toString().equals("")) {
                model.addAttribute("errMsg", "用户没有任何模块权限，请联系管理员！");
                return "login";
            } else {
                mIds = moduleIds.toString();
                if (mIds.substring(0, 1).equals(",")) {
                    mIds = mIds.substring(1, mIds.length());
                }
            }
            String userId = userMap.get("id").toString();
            UserSession userSession = getUserSession(userId, code, userMap.get("name").toString(),
                    userMap.get("role_id").toString(), userMap.get("role_name").toString(), mIds);
            /* 记录用户登录信息 */
            String ip = IpUtils.getRequestIpAddress(request);
            userMap.put("last_ip", ip);
            userMap.put("last_time", System.currentTimeMillis() / 1000);
            userModel.update(userMap);
            // 把用户信息保存到session
            session.setAttribute("session_user", userSession);
            LogUtils.saveCMSLog(ip, userId, "登录，用户[" + code + "][" + userSession.getName() + "]已成功登录");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/sys/home.html";
    }

    /***
     * 获取用户的登录session，包括角色，模块权限，工作流权限
     *
     * @param id       用户id
     * @param code     用户编号
     * @param name     用户姓名
     * @param roleId   用户角色id
     * @param roleName 用户角色名称
     * @param mIds     用户拥有的模块id列表，","隔开
     * @return 用户session
     */
    private UserSession getUserSession(String id, String code, String name, String roleId, String roleName, String mIds) {
        UserSession userSession = new UserSession();
        // 模块
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("id", FilterType.In, mIds));
        List<Map<String, Object>> moduleMaps = JdbcModelCache.getInstance().get("model_login_module").selectMaps(filters);
        // 按钮
        filters.clear();
        filters.add(new Filter("parent_id", FilterType.Eq, roleId));
        List<Map<String, Object>> actionMaps = JdbcModelCache.getInstance().get("model_sys_role_action").selectMaps(filters);
        // 列
        List<Map<String, Object>> columnMaps = JdbcModelCache.getInstance().get("model_sys_role_column").selectMaps(filters);
        // 行级权限
        filters.clear();
        filters.add(new Filter("parent_id", FilterType.Eq, id));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("column", OrderType.Desc));
        List<Map<String, Object>> rowFilterMaps = JdbcModelCache.getInstance().get("model_sys_user_row_filter").selectMaps(filters);
        // 生成权限
        Map<String, ModulePermission> permission = new HashMap<>();
        for (Map<String, Object> module : moduleMaps) {
            String moduleId = module.get("id").toString();
            permission.put(moduleId, getModulePermission(id, moduleId, (String) module.get("name"),
                    (String) module.get("view_code"), module.get("workflow_id"), actionMaps, columnMaps, rowFilterMaps));
        }
        userSession.setId(id);
        userSession.setCode(code);
        userSession.setName(name);
        userSession.setRoleName(roleName);
        userSession.setPermission(permission);
        Gson gson = new Gson();
        userSession.setModule(gson.toJson(moduleMaps));
        return userSession;
    }

    /**
     * 获取用户一个模块的权限，包括页面的列，操作，行级数据权限和工作流权限
     *
     * @param id            用户id
     * @param moduleId      模块的id
     * @param moduleName    模块的名称
     * @param viewCode      模块对应的view的编号
     * @param workFlowId    模块对应的工作流id
     * @param actionMaps    用户所有模块没有权限的按钮
     * @param columnMaps    用户所有模块没有权限的列
     * @param rowFilterMaps 用户所有模块的行级权限
     * @return 一个模块的权限
     */
    private ModulePermission getModulePermission(String id, String moduleId, String moduleName, String viewCode, Object workFlowId,
                                                 List<Map<String, Object>> actionMaps, List<Map<String, Object>> columnMaps,
                                                 List<Map<String, Object>> rowFilterMaps) {
        ModulePermission modulePermission = new ModulePermission();
        modulePermission.setModelName(moduleName);
        if (StringUtils.isBlank(viewCode)) {
            return modulePermission;
        }
        View view = ViewCache.getInstance().get(viewCode);
        modulePermission.setView(view);
        if (null != view) {
            // 模块对应的JdbcModel
            JdbcModel jdbcModel = JdbcModelCache.getInstance().get(view.getModelName());
            modulePermission.setJdbcModel(jdbcModel);
            // 模块对应model和view的主键名称
            modulePermission.setPkName(jdbcModel.getPkName());
        }
        // 生成模块按钮的权限
        if (null != actionMaps) {
            Map<String, String> actions = new HashMap<>();
            actionMaps.stream().filter(map -> moduleId.equals(map.get("module_id").toString())).forEach(map -> actions.put(map.get("value").toString(), map.get("value").toString()));
            modulePermission.setActions(actions);
        }
        // 生成模块列的权限
        if (null != columnMaps) {
            Map<String, String> columns = new HashMap<>();
            columnMaps.stream().filter(map -> moduleId.equals(map.get("module_id").toString())).forEach(map -> columns.put(map.get("value").toString(), map.get("value").toString()));
            modulePermission.setColumns(columns);
        }
        // 生成模块行级过滤权限
        if (null != rowFilterMaps) {
            Map<String, String> rowFilters = new HashMap<>();
            rowFilterMaps.stream().filter(map -> moduleId.equals(map.get("module_id").toString())).forEach(map -> rowFilters.put(map.get("column").toString(), map.get("value").toString().substring(1)));
            modulePermission.setRowFilters(rowFilters);
        }
        // 生成模块工作流权限
        if (null != workFlowId) {
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("id", FilterType.Eq, workFlowId));
            Map<String, Object> workFlowMap = JdbcModelCache.getInstance().get("model_sys_workflow").selectMap(filters);
            if (null != workFlowMap) {
                WorkFlow workFlow = new WorkFlow();
                workFlow.setId(workFlowId.toString());
                workFlow.setField(workFlowMap.get("field").toString());
                workFlow.setUserField(workFlowMap.get("user_field").toString());
                workFlow.setTimeField(workFlowMap.get("time_field").toString());
                // 获取工作流节点，并分类添加到map中，以value为key
                Map<String, WorkFlowStep> workFlowStepMap = new HashMap<>();
                filters.clear();
                filters.add(new Filter("parent_id", FilterType.Eq, workFlowId));
                List<Order> orders = new ArrayList<>();
                orders.add(new Order("type", OrderType.Asc));
                List<Map<String, Object>> list = JdbcModelCache.getInstance().get("model_sys_workflow_step").selectMaps(filters, orders);
                String userId = "," + id + ",";
                //获取工作流起点加入权限，拥有模块权限的用户都可以拥有工作流起点权限
                for (Map<String, Object> map : list) {
                    Integer type = Integer.parseInt(map.get("type").toString());
                    String userIds = (String) map.get("user_ids");
                    // 如果是起点或者登录用户拥有审核权限，则将步骤加入权限
                    if (type == 0 || (null != userIds && StringUtils.contains(userIds, userId))) {
                        WorkFlowStep workFlowStep = new WorkFlowStep();
                        workFlowStep.setName(map.get("name").toString());
                        String value = map.get("value").toString();
                        workFlowStep.setValue(value);
                        workFlowStep.setType(type);
                        workFlowStep.setNextValue(getNextWorkFlowStepValue(list, map.get("id").toString()));
                        workFlowStepMap.put(value, workFlowStep);
                    }
                }
                workFlow.setStepMap(workFlowStepMap);
                modulePermission.setWorkFlow(workFlow);
            }
        }
        return modulePermission;
    }

    /**
     * 根据上一步流程id获取下一步流程值
     *
     * @param list 工作流节点列表
     * @param id   需要取下一步节点值的节点的id
     * @return 下一步节点状态值
     */
    private String getNextWorkFlowStepValue(List<Map<String, Object>> list, String id) {
        String result = null;
        for (Map<String, Object> map : list) {
            if (id.equals(map.get("last_step_id").toString())) {
                result = map.get("value").toString();
                break;
            }
        }
        return result;
    }

    /**
     * 获取用户的菜单
     *
     * @param request  请求
     * @param response 用户菜单json数据
     */
    @RequestMapping(value = "/getMenu.do")
    public void getMenu(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("session_user");
        writeResponse(response, userSession.getModule());
    }

    /**
     * 登出，记录日志清空session
     *
     * @param request 请求
     * @return 返回到登录页面
     */
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("session_user");
        String userId = userSession.getId();
        String code = userSession.getCode();
        String name = userSession.getName();
        // 清空session
        session.removeAttribute("session_user");
        LogUtils.saveCMSLog(IpUtils.getRequestIpAddress(request), userId, "登出，用户[" + code + "][" + name + "]已成功登出");
        return "login";
    }

}
