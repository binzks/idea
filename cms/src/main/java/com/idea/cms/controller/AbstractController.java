package com.idea.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idea.cms.support.ModulePermission;
import com.idea.cms.support.UserSession;
import com.idea.common.view.View;
import com.idea.common.view.ViewColumn;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public abstract class AbstractController extends BaseController {

    @RequestMapping(value = {"/list{mid}{page}", "/list{mid}-{page}"})
    public String getList(@PathVariable String mid, @PathVariable Integer page, Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            // 获取页面查询条件
            List<Filter> filters = initFilters(model, request, view, modulePermission.getRowFilters());
            // 初始化页面分页信息
            Integer start = initPages(model, request, view, page, filters);
            // 获取数据
            //	List<Map<String, Object>> dataList = view.selectMaps(filters, start, view.getRowSize());
            model.addAttribute("mid", mid);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("pk", view.getPkName());
            model.addAttribute("powerActions", modulePermission.getActions());
            model.addAttribute("actions", view.getActions());
            model.addAttribute("powerColumns", modulePermission.getColumns());
            model.addAttribute("columns", view.getColumns());
            //	model.addAttribute("dataList", dataList);
            return "/system/list";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    @RequestMapping(value = "/add{mid}")
    public String addData(@PathVariable String mid, Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            model.addAttribute("mid", mid);
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("columns", view.getColumns());
            return "/system/add";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    @RequestMapping(value = "/edit{mid}-{id}")
    public String editData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                           HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter(view.getPkName(), FilterType.Eq, id));
            //	Map<String, Object> map = view.selectMap(filters);
            model.addAttribute("mid", mid);
            model.addAttribute("id", id);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("baseUrl", this.getClass().getAnnotation(RequestMapping.class).value()[0]);
            model.addAttribute("columns", view.getColumns());
            //	model.addAttribute("data", map);
            return "/system/edit";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    @RequestMapping(value = {"save{mid}{id}", "/save{mid}-{id}"}, method = RequestMethod.POST)
    public String saveData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                           HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            String pkName = view.getPkName();
            String baseUrl = this.getClass().getAnnotation(RequestMapping.class).value()[0];
            List<ViewColumn> columns = view.getColumns();
            Map<String, Object> dataMap = new HashMap<>();
            for (ViewColumn viewColumn : columns) {
                String columnName = viewColumn.getName();
                if (columnName.equals(pkName)) {
                    continue;
                }
                Object value = request.getParameter(columnName);
                // 如果是新增数据，则填入默认值，如果是修改数据则不处理null数据
                if (StringUtils.isBlank(id) && null == value) {
                    value = getDefaultValue(viewColumn.getDefaultValue(),
                            (UserSession) request.getSession().getAttribute("session_user"));
                }
                dataMap.put(viewColumn.getName(), value);
            }
            if (StringUtils.isNotBlank(id)) {
                dataMap.put(pkName, id);
                //	view.update(dataMap);
            } else {
                //	view.insert(dataMap);
            }
            return "redirect:" + baseUrl + "/list" + mid + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    private Object getDefaultValue(String value, UserSession userSession) {
        if (null == value) {
            return null;
        } else if (value.equals("now")) {
            return System.currentTimeMillis() / 1000;
        } else if (value.equals("user.id")) {
            return userSession.getId();
        } else if (value.equals("user.name")) {
            return userSession.getName();
        } else {
            return value;
        }
    }

    @RequestMapping(value = "/detail{mid}-{id}")
    public String detailData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter(view.getPkName(), FilterType.Eq, id));
            //Map<String, Object> map = view.selectMap(filters);
            model.addAttribute("title", view.getTitle());
            model.addAttribute("columns", view.getColumns());
            model.addAttribute("powerColumns", modulePermission.getColumns());
            //model.addAttribute("data", map);
            return "/system/detail";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

    @RequestMapping(value = "/del{mid}-{id}", method = RequestMethod.POST)
    public String delData(@PathVariable String mid, @PathVariable String id, Model model, HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            ModulePermission modulePermission = getModulePermission(mid, request);
            View view = modulePermission.getView();
            if (view.getBackupOnDel()) {
                backUpDelData(view, mid, id);
            }
            //	view.deleteById(id);
            return "redirect:" + this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/list" + mid
                    + ".html";
        } catch (Exception e) {
            model.addAttribute("msg", "错误[" + e + "]");
            return "/system/error";
        }
    }

}
