package com.idea.cms.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idea.cms.support.ModulePermission;
import com.idea.cms.support.UserSession;
import com.idea.common.cache.JdbcModelCache;
import com.idea.common.view.View;
import com.idea.common.view.ViewColumn;
import com.idea.common.view.ViewColumnTag;
import com.idea.common.view.ViewPage;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.model.Column;
import com.idea.framework.jdbc.support.model.Filter;
import com.idea.framework.jdbc.support.model.FilterType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.google.gson.Gson;

public class BaseController {

    public final static String ENCODING = "UTF-8";

    private Logger logger = Logger.getLogger(BaseController.class);

    /**
     * 获取用户对应模块的权限，用于生成用户对应模块页面
     *
     * @param moduleId 模块id
     * @param request  请求
     * @return 返回登录用户对应的模块的权限，包括按钮、列、工作流等等
     * @throws RuntimeException
     */
    public ModulePermission getModulePermission(String moduleId, HttpServletRequest request) throws RuntimeException {
        UserSession userSession = (UserSession) request.getSession().getAttribute("session_user");
        ModulePermission modulePermission = userSession.getModulePermission(moduleId);
        if (null == modulePermission) {
            logger.error("getModulePermission:模块[" + moduleId + "]不存在或者没有授权！");
            throw new RuntimeException("模块[" + moduleId + "]不存在或者没有授权，请联系管理员！");
        }
        return modulePermission;
    }

    /***
     * 初始化页面的查询条件，并将查询条件值填入model的search属性，供页面赋值
     * 先判断如果model中没有列，则不过滤，如果列有行级过滤，则先加入行级过滤值，如果列有查询过滤，则加入查询过滤值，如果过滤值为空表示不过滤
     *
     * @param model       页面model
     * @param request     页面请求
     * @param viewColumns view的列定义
     * @param columns     view对应的model的列定义
     * @param rowFilters  模块的行级过滤
     * @return 返回过滤列表
     * @throws Exception 字符串转时间异常
     */
    public List<Filter> initFilters(Model model, HttpServletRequest request, List<ViewColumn> viewColumns, Map<String, Column> columns, Map<String, String> rowFilters) throws Exception {
        Map<String, Object> searchMap = new HashMap<>();
        List<Filter> filters = new ArrayList<>();
        for (int i = 0; i < viewColumns.size(); i++) {
            ViewColumn viewColumn = viewColumns.get(i);
            Column column = columns.get(viewColumn.getName());
            if (null == column) {
                continue;
            }
            if (viewColumn.isRowFilter()) {
                String rowValue = null;
                for (Entry<String, String> entry : rowFilters.entrySet()) {
                    if (entry.getKey().equals(column.getName())) {
                        rowValue = entry.getValue();
                        break;
                    }
                }
                filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.In, rowValue));
            }
            if (!viewColumn.isSearch()) {
                continue;
            }
            // 如果是日期或者时间则特殊处理，取范围值，如果是text则取like，其他等于
            if (ViewColumnTag.Date == viewColumn.getTag()) {
                String begin = getParameterString(request, viewColumn.getName() + "_begin");
                String end = getParameterString(request, viewColumn.getName() + "_end");
                if (StringUtils.isNotBlank(begin) && StringUtils.isNotBlank(end)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Between,
                            simpleDateFormat.parse(begin.trim() + " 00:00:00").getTime() / 1000 + "," + simpleDateFormat.parse(end.trim() + " 23:59:59").getTime() / 1000));
                }
            } else if (ViewColumnTag.DateTime == viewColumn.getTag()) {
                String begin = getParameterString(request, viewColumn.getName() + "_begin");
                String end = getParameterString(request, viewColumn.getName() + "_end");
                if (StringUtils.isNotBlank(begin) && StringUtils.isNotBlank(end)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Between,
                            simpleDateFormat.parse(begin).getTime() / 1000 + "," + simpleDateFormat.parse(end).getTime() / 1000));
                }
            } else {
                String value = getParameterString(request, viewColumn.getName());
                if (StringUtils.isNotBlank(value)) {
                    if (ViewColumnTag.Text == viewColumn.getTag()) {
                        filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Like, value));
                    } else {
                        filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Eq, value));
                    }
                }
            }

            String value = request.getParameter(viewColumn.getName());
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (ViewColumnTag.Text == viewColumn.getTag()) {
                filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Like, value));
            } else if (ViewColumnTag.Date == viewColumn.getTag() || ViewColumnTag.DateTime == viewColumn.getTag()) {
                String[] date = value.split("-");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                long begin = simpleDateFormat.parse(date[0].trim() + " 00:00:00").getTime() / 1000;
                long end = simpleDateFormat.parse(date[1].trim() + " 23:59:59").getTime() / 1000;
                filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Between, begin + "," + end));
            } else {
                filters.add(new Filter(column.getJoinName(), column.getName(), FilterType.Eq, value));
            }
            searchMap.put(column.getName(), value);
        }
        model.addAttribute("search", searchMap);
        return filters;
    }

    /**
     * 生成页面分页，并将分页填入model的pages属性，供页面使用
     *
     * @param model
     * @param request
     * @param view
     * @param page
     * @return 返回页面起始的数据行数，供实际获取分页数据的时候使用的开始数据行
     */
    public Integer initPages(Model model, HttpServletRequest request, View view, Integer page, Integer totalCount) {
        ViewPage viewPage = new ViewPage();
        viewPage.setCurrentPage(page);
        viewPage.setRowSize(view.getRowSize());
        viewPage.setTotalRows(totalCount);
        model.addAttribute("pages", viewPage);
        return viewPage.getStart();
    }

    /**
     * 将value写入返回response
     *
     * @param response 返回对象
     * @param value    写入值
     */
    public void writeResponse(HttpServletResponse response, String value) {
        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            response.setHeader("Pragma", "No-cache");
            byte[] bytes = value.getBytes(ENCODING);
            response.setCharacterEncoding(ENCODING);
            response.setContentType("text/plain");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            logger.error("writeResponse错误[" + e.getMessage() + "]");
            e.printStackTrace();
        }
    }

    /**
     * 获取请求参数值，中文使用utf-8编码转换，防止乱码
     *
     * @param request 请求
     * @param key     参数名称
     * @return 参数值
     */
    public String getParameterString(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            try {
                value = new String(value.getBytes("iso-8859-1"), ENCODING);
            } catch (UnsupportedEncodingException e) {
                logger.error("getParameterString错误[" + e.getMessage() + "]");
                e.printStackTrace();
            }
        }
        return value;
    }

    /***
     * 删除数据前，将整条数据的json格式内容保存到备份数据表，用户有删除功能的数据备份
     *
     * @param mid       模块id
     * @param viewName  view的名称
     * @param tableName 表名
     * @param data      删除的数据
     */
    public void backUpDelData(String mid, String viewName, String tableName, String data) {
        Map<String, Object> backupMap = new HashMap<>();
        Gson gson = new Gson();
        try {
            backupMap.put("mid", mid);
            backupMap.put("view", viewName);
            backupMap.put("table", tableName);
            backupMap.put("create_time", System.currentTimeMillis() / 1000);
            backupMap.put("data", data);
            JdbcModelCache.getInstance().get("sys_cms_del_backup").insert(backupMap);
        } catch (Exception e) {
            logger.error("backUpDelData数据[" + gson.toJson(backupMap) + "]错误[" + e.getMessage() + "]");
        }
    }

    /**
     * 生成树节点信息
     *
     * @param id       自己的ID
     * @param parentId 父ID
     * @param name     名称
     * @return 返回一个树节点
     */
    public Map<String, Object> getTreeNode(String id, String parentId, String name, boolean checked) {
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("id", id);
        nodeMap.put("pId", parentId);
        nodeMap.put("name", name);
        nodeMap.put("open", false);
        nodeMap.put("checked", checked);
        return nodeMap;
    }
}
