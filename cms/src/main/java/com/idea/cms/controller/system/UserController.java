package com.idea.cms.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idea.cms.controller.AbstractController;
import com.idea.cms.support.ModulePermission;
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.cache.ViewCache;
import com.idea.common.view.View;
import com.idea.common.view.ViewColumn;
import com.idea.framework.Utils.EncryptUtils;
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
@RequestMapping(value = "/user")
public class UserController extends AbstractController {

    private String modulePrefix = "@m@";
    private String columnPrefix = "@c@";
    private String rowValuePrefix = "@r@";

    @RequestMapping(value = "/chg_psw{mid}-{id}") // 密码修改
    public String changePassword(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
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
            return "system/user/chgPsw";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/save_psw{mid}-{id}")
    public String savePassword(@PathVariable String mid, @PathVariable String id, Model model,
                               HttpServletRequest request) {
        try {
            String pwd = request.getParameter("password");
            String password = EncryptUtils.md5(EncryptUtils.md5(pwd));
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", id);
            userMap.put("password", password);
            userMap.put("modify_time", System.currentTimeMillis() / 1000);
            JdbcModelCache.getInstance().get("model_sys_user").update(userMap);
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/row_filter{mid}-{id}")
    public String rowFilter(@PathVariable String mid, @PathVariable String id, Model model,
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
            return "system/user/rowFilter";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/save_row_filter{mid}-{id}")
    public String savePower(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
        try {
            // 删除原来的过滤权限
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("parent_id", FilterType.Eq, id));
            JdbcModel dataModel = JdbcModelCache.getInstance().get("model_sys_user_row_filter");
            dataModel.delete(filters);
            // 添加新的过滤权限
            String rowValChecked = request.getParameter("rowValChecked");
            if (StringUtils.isNotBlank(rowValChecked)) {
                String[] rowList = rowValChecked.split(",");
                List<Map<String, Object>> list = new ArrayList<>();
                String lastColumn = null;
                StringBuilder values = new StringBuilder();
                for (int i = 0; i < rowList.length; i++) {
                    String row = rowList[i];
                    int moduleIndex = row.indexOf(modulePrefix);
                    int columnIndex = row.indexOf(columnPrefix);
                    int valueIndex = row.indexOf(rowValuePrefix);
                    String moduleId = StringUtils.substring(row, moduleIndex + modulePrefix.length(), columnIndex);
                    String column = StringUtils.substring(row, columnIndex + columnPrefix.length(), valueIndex);
                    String value = StringUtils.substring(row, valueIndex + rowValuePrefix.length(), row.length());
                    if (null == lastColumn) {
                        lastColumn = column;
                        values.append(",");
                    }
                    if (lastColumn.equals(column)) {
                        values.append(value).append(",");
                    } else {
                        list.add(getRowFilterMap(id, moduleId, lastColumn, values.toString()));
                        lastColumn = column;
                        values.setLength(0);
                        values.append(",").append(value).append(",");
                    }
                    if (i == rowList.length - 1 && values.length() > 0) {
                        list.add(getRowFilterMap(id, moduleId, lastColumn, values.toString()));
                    }
                }
                dataModel.batchInsert(list);
            }
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    private Map<String, Object> getRowFilterMap(String id, String moduleId, String column, String value) {
        Map<String, Object> rowFilter = new HashMap<>();
        rowFilter.put("parent_id", id);
        rowFilter.put("module_id", moduleId);
        rowFilter.put("column", column);
        rowFilter.put("value", value);
        return rowFilter;
    }

    @RequestMapping(value = "/getTree.do")
    public void getRowFilterTree(HttpServletRequest request, HttpServletResponse response) {
        // JSON树
        String userId = request.getParameter("id");
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("id", FilterType.Eq, userId));
        Map<String, Object> userMap = JdbcModelCache.getInstance().get("model_sys_user").selectMap(filters);
        filters.clear();
        filters.add(new Filter("id", FilterType.Eq, userMap.get("role_id")));
        Map<String, Object> roleMap = JdbcModelCache.getInstance().get("model_sys_role").selectMap(filters);
        String moduleIds = (String) roleMap.get("module_ids");
        List<Map<String, Object>> result = initRowFilterTree(userId, moduleIds);
        Gson gson = new Gson();
        writeResponse(response, gson.toJson(result));
    }

    /**
     * 根据用户的模块权限，生成模块权限对应的列的行级过滤树，只显示有行级过滤列的模块
     *
     * @param userId    登录用户id
     * @param moduleIds 模块id
     * @return 行级过滤树
     */
    private List<Map<String, Object>> initRowFilterTree(String userId, String moduleIds) {
        if (StringUtils.isBlank(moduleIds)) {
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("parent_id", FilterType.Eq, userId));
        List<Map<String, Object>> rowFilterMaps = JdbcModelCache.getInstance().get("model_sys_user_row_filter").selectMaps(filters);
        filters.clear();
        filters.add(new Filter("id", FilterType.In, moduleIds.substring(1, moduleIds.length())));
        List<Map<String, Object>> moduleMaps = JdbcModelCache.getInstance().get("model_login_module").selectMaps(filters);
        for (Map<String, Object> map : moduleMaps) {
            String viewCode = (String) map.get("view_code");
            if (StringUtils.isBlank(viewCode)) {
                continue;
            }
            String mid = map.get("id").toString();
            boolean checked = false;
            boolean isAdd = false;
            View view = ViewCache.getInstance().get(viewCode);
            for (ViewColumn viewColumn : view.getColumns()) {
                if (viewColumn.isRowFilter()) {
                    isAdd = true;
                    String cid = viewColumn.getName();
                    for (Entry<String, String> entry : viewColumn.getItems().entrySet()) {
                        boolean rowChecked = getChecked(rowFilterMaps, entry.getKey(), mid, cid);
                        if (rowChecked) {
                            checked = true;
                        }
                        result.add(
                                getTreeNode(modulePrefix + mid + columnPrefix + cid + rowValuePrefix + entry.getKey(),
                                        columnPrefix + cid, entry.getValue(), rowChecked));
                    }
                    result.add(getTreeNode(columnPrefix + cid, modulePrefix + mid, viewColumn.getDescribe(), checked));
                }
            }
            if (isAdd) {
                result.add(getTreeNode(modulePrefix + mid, modulePrefix + map.get("parent_id").toString(),
                        map.get("name").toString(), checked));
            }
        }
        return result;
    }

    /**
     * 获取行级过滤值是否需要选中
     *
     * @param list     待检查数据
     * @param value    检查值
     * @param moduleId 模块id
     * @param column   model列
     * @return 是否选中
     */
    private boolean getChecked(List<Map<String, Object>> list, String value, String moduleId, String column) {
        boolean result = false;
        for (Map<String, Object> map : list) {
            if (moduleId.equals(map.get("module_id").toString()) && column.equals(map.get("column").toString())) {
                if (StringUtils.contains(map.get("value").toString(), "," + value + ",")) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    @RequestMapping(value = "/info")
    public String info(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            // ModulePermission modulePermission = getModulePermission(mid,
            // request);
            // View view = modulePermission.getView();
            // String pkName = view.getPkName();
            // String baseUrl =
            // this.getClass().getAnnotation(RequestMapping.class).value()[0];
            // Map<String, Object> dataMap = new HashMap<>();
            // dataMap.put("code", request.getParameter("code"));
            // dataMap.put("name", request.getParameter("name"));
            // String password = request.getParameter("password");
            // password = EncryptUtils.md5(EncryptUtils.md5(password));
            // dataMap.put("password", password);
            // dataMap.put("phone", request.getParameter("phone"));
            // dataMap.put("email", request.getParameter("email"));
            // dataMap.put("roleid", request.getParameter("roleid"));
            // dataMap.put("status", request.getParameter("status"));
            // if (StringUtils.isNotBlank(id)) {
            // dataMap.put(pkName, id);
            // view.update(dataMap);
            // } else {
            // view.insert(dataMap);
            // }
            return "/system/user/info";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

}
