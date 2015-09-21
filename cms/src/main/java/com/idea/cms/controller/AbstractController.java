package com.idea.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.idea.cms.support.ModulePermission;
import com.idea.cms.support.UserSession;
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.view.View;
import com.idea.common.view.ViewColumn;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public abstract class AbstractController extends BaseController {

    @RequestMapping(value = {"/list{mid}{page}", "/list{mid}-{page}"})
    public String getList(@PathVariable String mid, @PathVariable Integer page, Model model, HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            JdbcModel jdbcModel = modulePermission.getJdbcModel();
            // 获取页面查询条件
            List<Filter> filters = initFilters(model, request, view.getColumns(), jdbcModel.getColumns(), modulePermission.getRowFilters());
            int totalCount = jdbcModel.getTotalCount(filters);
            // 初始化页面分页信息
            Integer start = initPages(model, request, view, page, totalCount);
            // 获取数据
            List<Map<String, Object>> dataList = modulePermission.getJdbcModel().selectMaps(filters, start, view.getRowSize());
            int optWidth = view.getActions().size() * 23;
            model.addAttribute("mid", mid);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("pk", modulePermission.getPkName());
            model.addAttribute("powerActions", modulePermission.getActions());
            model.addAttribute("actions", view.getActions());
            model.addAttribute("powerColumns", modulePermission.getColumns());
            model.addAttribute("columns", view.getColumns());
            model.addAttribute("optWidth", optWidth);
            model.addAttribute("dataList", dataList);
            return "/system/template/list";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/add{mid}")
    public String addData(@PathVariable String mid, Model model, HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            model.addAttribute("mid", mid);
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("columns", view.getColumns());
            return "/system/template/add";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    @RequestMapping(value = "/edit{mid}-{id}")
    public String editData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter(modulePermission.getPkName(), FilterType.Eq, id));
            Map<String, Object> map = modulePermission.getJdbcModel().selectMap(filters);
            model.addAttribute("mid", mid);
            model.addAttribute("id", id);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("columns", view.getColumns());
            model.addAttribute("data", map);
            return "/system/template/edit";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    /**
     * 保存数据，如果id为空则新增，否则修改
     *
     * @param mid     模块id
     * @param id      数据id
     * @param model   页面model
     * @param request 客户端请求
     * @return 页面地址
     */
    @RequestMapping(value = {"save{mid}{id}", "/save{mid}-{id}"}, method = RequestMethod.POST)
    public String saveData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            String pkName = modulePermission.getPkName();
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            List<ViewColumn> columns = view.getColumns();
            Map<String, Object> dataMap = new HashMap<>();
            for (ViewColumn viewColumn : columns) {
                String columnName = viewColumn.getName();
                if (columnName.equals(pkName)) {
                    continue;
                }
                String value = request.getParameter(columnName);
                // 如果数据为null或者空则填入默认值
                if (StringUtils.isBlank(value)) {
                    value = getDefaultValue(viewColumn.getDefaultValue(),
                            (UserSession) request.getSession().getAttribute("session_user"));
                }
                dataMap.put(viewColumn.getName(), value);
            }
            JdbcModel jdbcModel = modulePermission.getJdbcModel();
            if (StringUtils.isNotBlank(id)) {
                dataMap.put(pkName, id);
                jdbcModel.update(dataMap);
            } else {
                jdbcModel.insert(dataMap);
            }
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    /**
     * 获取view定义的默认值
     *
     * @param value       默认值
     * @param userSession 登录用户session
     * @return 返回真实默认值
     */
    private String getDefaultValue(String value, UserSession userSession) {
        if (null == value) {
            return null;
        } else if (value.equals("now")) {
            return String.valueOf(System.currentTimeMillis() / 1000);
        } else if (value.equals("user.id")) {
            return userSession.getId();
        } else if (value.equals("user.name")) {
            return userSession.getName();
        } else {
            return value;
        }
    }

    /**
     * 获取数据详细内容
     *
     * @param mid      模块id
     * @param id       数据id
     * @param model    页面model
     * @param request  请求
     * @param response 请求响应
     * @return 返回页面地址
     */
    @RequestMapping(value = "/detail{mid}-{id}")
    public String detailData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter(modulePermission.getPkName(), FilterType.Eq, id));
            Map<String, Object> map = modulePermission.getJdbcModel().selectMap(filters);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("columns", view.getColumns());
            model.addAttribute("powerColumns", modulePermission.getColumns());
            model.addAttribute("data", map);
            return "/system/template/detail";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }

    /***
     * 删除数据
     *
     * @param mid      模块id
     * @param id       数据id
     * @param model    写入页面model
     * @param request  客户端请求
     * @param response 客户端返回
     * @return 返回页面地址
     */
    @RequestMapping(value = "/del{mid}-{id}", method = RequestMethod.POST)
    public String delData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            List<Filter> filters = new ArrayList<>();
            JdbcModel jdbcModel = modulePermission.getJdbcModel();
            filters.add(new Filter(modulePermission.getPkName(), FilterType.Eq, id));
            Map<String, Object> map = jdbcModel.selectMap(filters, "*");
            String data = new Gson().toJson(map);
            backUpDelData(mid, view.getName(), jdbcModel.getTableName(), data);
            modulePermission.getJdbcModel().deleteById(id);
            return "redirect:" + this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/list" + mid
                    + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/template/error";
        }
    }


}
