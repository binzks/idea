package com.idea.cms.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idea.cms.controller.AbstractController;
import com.idea.cms.support.ModulePermission;
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.cache.ViewCache;
import com.idea.common.view.View;
import com.idea.common.view.ViewAction;
import com.idea.common.view.ViewColumn;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends AbstractController {

    private String modulePrefix = "@m@";
    private String columnPrefix = "@c@";
    private String actionPrefix = "@a@";

    @RequestMapping(value = "/empower{mid}-{id}")
    public String authorization(@PathVariable String mid, @PathVariable String id, Model model,
                                HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter(modulePermission.getPkName(), FilterType.Eq, id));
            Map<String, Object> map = modulePermission.getJdbcModel().selectMap(filters);
            model.addAttribute("mid", mid);
            model.addAttribute("id", id);
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("code", map.get("code"));
            model.addAttribute("name", map.get("name"));
            return "/system/role/empower";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/save_power{mid}-{id}")
    public String savePower(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
        try {
            String moduleChecked = request.getParameter("moduleChecked");
            String columnUnChecked = request.getParameter("columnUnChecked");
            String actionUnChecked = request.getParameter("actionUnChecked");
            String moduleIds = moduleChecked.replace(modulePrefix, "");
            // 更新角色模块权限
            if (StringUtils.isNotBlank(moduleIds)) {
                Map<String, Object> roleMap = new HashMap<>();
                roleMap.put("id", id);
                roleMap.put("module_ids", moduleIds);
                roleMap.put("modify_time", System.currentTimeMillis() / 1000);
                JdbcModelCache.getInstance().get("model_sys_role").update(roleMap);
            }
            // 删除原先行列权限
            JdbcModel columnModel = JdbcModelCache.getInstance().get("model_sys_role_column");
            JdbcModel actionModel = JdbcModelCache.getInstance().get("model_sys_role_action");
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("parent_id", FilterType.Eq, id));
            columnModel.delete(filters);
            actionModel.delete(filters);
            // 保存角色列权限
            if (StringUtils.isNotBlank(columnUnChecked)) {
                List<Map<String, Object>> roleColumns = new ArrayList<>();
                String[] columns = columnUnChecked.split(",");
                for (String column : columns) {
                    String[] one = column.split(columnPrefix);
                    if (one.length == 2) {
                        String moduleId = one[0];
                        if (StringUtils.contains(moduleIds, "," + moduleId + ",")) {
                            Map<String, Object> roleColumn = new HashMap<>();
                            roleColumn.put("parent_id", id);
                            roleColumn.put("module_id", moduleId);
                            roleColumn.put("value", one[1]);
                            roleColumns.add(roleColumn);
                        }
                    }
                }
                columnModel.batchInsert(roleColumns);
            }
            // 保存角色操作权限
            if (StringUtils.isNotBlank(actionUnChecked)) {
                List<Map<String, Object>> roleActions = new ArrayList<>();
                String[] actions = actionUnChecked.split(",");
                for (String action : actions) {
                    String[] one = action.split(actionPrefix);
                    if (one.length == 2) {
                        String moduleId = one[0];
                        if (StringUtils.contains(moduleIds, "," + moduleId + ",")) {
                            Map<String, Object> roleAction = new HashMap<>();
                            roleAction.put("parent_id", id);
                            roleAction.put("module_id", moduleId);
                            roleAction.put("value", one[1]);
                            roleActions.add(roleAction);
                        }
                    }
                }
                actionModel.batchInsert(roleActions);
            }
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/getTree.do")
    public void getModuleTree(HttpServletRequest request, HttpServletResponse response) {
        // JSON树
        String roleId = request.getParameter("id");
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("status", FilterType.Eq, "1"));
        List<Map<String, Object>> moduleMaps = JdbcModelCache.getInstance().get("model_sys_module").selectMaps(filters);
        List<Map<String, Object>> returnModules = initModuleTree(moduleMaps, roleId);
        Gson gson = new Gson();
        writeResponse(response, gson.toJson(returnModules));
    }

    private List<Map<String, Object>> initModuleTree(List<Map<String, Object>> data, String roleId) {
        if (null == data) {
            return null;
        }
        // 获取原先模块权限
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("id", FilterType.Eq, roleId));
        Map<String, Object> roleMap = JdbcModelCache.getInstance().get("model_sys_role").selectMap(filters);
        String moduleIds = (String) roleMap.get("module_ids");
        if (null == moduleIds) {
            moduleIds = "";
        }
        // 获取原先列权限
        filters.clear();
        filters.add(new Filter("parent_id", FilterType.Eq, roleId));
        List<Map<String, Object>> roleColumns = JdbcModelCache.getInstance().get("model_sys_role_column").selectMaps(filters);
        // 获取原先按钮权限
        List<Map<String, Object>> roleActions = JdbcModelCache.getInstance().get("model_sys_role_action").selectMaps(filters);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Map<String, Object> map : data) {
            String mid = map.get("id").toString();
            // 模块权限MAP
            boolean moduleChecked = false;
            if (StringUtils.contains(moduleIds, "," + mid + ",")) {
                moduleChecked = true;
            }
            mapList.add(getTreeNode(modulePrefix + mid, modulePrefix + map.get("parent_id").toString(),
                    map.get("name").toString(), moduleChecked));
            View view = ViewCache.getInstance().get((String) map.get("view_code"));
            if (null == view) {
                continue;
            }
            // 获取COLUMN
            List<ViewColumn> columns = view.getColumns();
            if (null != columns) {
                boolean columnChecked = false;
                String columnId = columnPrefix + mid;
                // 循环所有列，如果有权限则勾选，如果没有权限则不勾选
                for (ViewColumn column : columns) {
                    String columnName = column.getName();
                    if (getChecked(roleColumns, columnName, mid) && moduleChecked) {
                        mapList.add(getTreeNode(mid + columnPrefix + columnName, columnId,
                                column.getDescribe() + "(" + column.getName() + ")", true));
                        columnChecked = true;
                    } else {
                        mapList.add(getTreeNode(mid + columnPrefix + columnName, columnId,
                                column.getDescribe() + "(" + column.getName() + ")", false));
                    }
                }
                mapList.add(getTreeNode(columnId, modulePrefix + mid, "列权限", columnChecked));
            }
            // 获取ACTION
            List<ViewAction> actions = view.getActions();
            if (null != actions && moduleChecked) {
                String actionId = this.actionPrefix + mid;
                boolean actionChecked = false;
                for (ViewAction action : actions) {
                    String actionName = action.getName();
                    if (getChecked(roleActions, actionName, mid) && moduleChecked) {
                        mapList.add(getTreeNode(mid + actionPrefix + actionName, actionId, action.getDescribe(), true));
                        actionChecked = true;
                    } else {
                        mapList.add(
                                getTreeNode(mid + actionPrefix + actionName, actionId, action.getDescribe(), false));
                    }
                }
                mapList.add(getTreeNode(actionId, modulePrefix + mid, "按钮权限", actionChecked));
            }
        }
        return mapList;
    }

    /**
     * 判断权限的列和按钮是否需要勾选，如果权限中有值表示没有权限，不要勾选
     *
     * @param list  待校验的数据
     * @param value 检查值
     * @param id    模块id
     * @return 是否选中
     */
    private boolean getChecked(List<Map<String, Object>> list, String value, String id) {
        boolean result = true;
        for (Map<String, Object> map : list) {
            if (id.equals(map.get("module_id").toString())) {
                Object v = map.get("value");
                if (null != v && v.toString().equals(value)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

}
