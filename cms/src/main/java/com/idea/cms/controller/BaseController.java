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
import com.idea.common.view.View;
import com.idea.common.view.ViewColumn;
import com.idea.common.view.ViewPage;
import com.idea.framework.jdbc.support.model.Column;
import com.idea.framework.jdbc.support.model.Filter;
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
     * @param moduleId
     * @param request
     * @return 返回登录用户对应的模块的权限，包括按钮、列、工作流等等
     * @throws Exception
     */
    public ModulePermission getModulePermission(String moduleId, HttpServletRequest request) throws Exception {
        UserSession userSession = (UserSession) request.getSession().getAttribute("session_user");
        ModulePermission modulePermission = userSession.getModulePermission(moduleId);
        if (null == modulePermission) {
            logger.error("getModulePermission:模块[" + moduleId + "]不存在或者没有授权！");
            throw new Exception("模块[" + moduleId + "]不存在或者没有授权，请联系管理员！");
        }
        return modulePermission;
    }

    /**
     * 初始化页面的查询条件，并将查询条件值填入model的search属性，供页面赋值
     *
     * @param model
     * @param request
     * @param view
     * @return 返回构造好的查询条件list
     * @throws Exception
     */
    public List<Filter> initFilters(Model model, HttpServletRequest request, View view, Map<String, String> rowFilters) throws Exception {
        Map<String, Object> searchMap = new HashMap<>();
        List<Filter> filters = new ArrayList<Filter>();
        List<ViewColumn> columns = view.getColumns();
//        for (int i = 0; i < columns.size(); i++) {
//            ViewColumn viewColumn = columns.get(i);
//            Column column = view.getModelColumn(viewColumn.getName());
//            if (null == column) {
//                continue;
//            }
//            if (viewColumn.isRowfilter()) {
//                String rowValue = null;
//                for (Entry<String, String> entry : rowFilters.entrySet()) {
//                    if (entry.getKey().equals(column.getName())) {
//                        rowValue = entry.getValue();
//                        break;
//                    }
//                }
//                filters.add(new Filter(column.getJoinName(), column.getName(), "in", rowValue));
//            }
//            String value = request.getParameter(viewColumn.getName());
//            if (StringUtils.isBlank(value)) {
//                continue;
//            }
//
//            String tag = viewColumn.getTag();
//            if (tag.equals(ViewColumn.Tag.TEXT.toString())) {
//                filters.add(new Filter(column.getJoinName(), column.getName(), "like", value));
//            } else if (tag.equals(ViewColumn.Tag.DATE.toString()) || tag.equals(ViewColumn.Tag.DATETIME.toString())) {
//                String[] date = value.split("-");
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
//                long begin = simpleDateFormat.parse(date[0].trim() + " 00:00:00").getTime() / 1000;
//                long end = simpleDateFormat.parse(date[1].trim() + " 23:59:59").getTime() / 1000;
//                filters.add(new Filter(column.getJoinName(), column.getName(), "between", begin + "," + end));
//            } else {
//                filters.add(new Filter(column.getJoinName(), column.getName(), "=", value));
//            }
//            searchMap.put(column.getName(), value);
//        }
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
     * @param filters
     * @return 返回页面起始的数据行数，供实际获取分页数据的时候使用的开始数据行
     */
    public Integer initPages(Model model, HttpServletRequest request, View view, Integer page, List<Filter> filters) {
        ViewPage viewPage = new ViewPage();
        viewPage.setCurrentPage(page);
        viewPage.setRowSize(view.getRowSize());
       // int totalCount = view.getTotalCount(filters);
       // viewPage.setTotalRows(totalCount);
        model.addAttribute("pages", viewPage);
        return viewPage.getStart();
    }

    /**
     * 将value写入返回response
     *
     * @param response
     * @param value
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
     * @param request
     * @param key
     * @return
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

    /**
     * 删除数据前，将整条数据的json格式内容保存到备份数据表，用户有删除功能的数据备份
     *
     * @param view
     * @param mid
     * @param id
     */
    public void backUpDelData(View view, String mid, String id) {
        String data = "";
        try {
//            List<Filter> filters = new ArrayList<>();
//            filters.add(new Filter(view.getPkName(), "=", id));
//            Map<String, Object> map = view.selectMap(filters, "*");
//            Gson gson = new Gson();
//            data = gson.toJson(map);
//            Map<String, Object> backupMap = new HashMap<String, Object>();
//            backupMap.put("mid", mid);
//            backupMap.put("view", view.getName());
//            backupMap.put("table", view.getTableName());
//            backupMap.put("createtime", System.currentTimeMillis() / 1000);
//            backupMap.put("data", data);
//            Think.getModel("sys_cms_del_backup").insert(backupMap);
        } catch (Exception e) {
            logger.error("backUpDelData数据[" + view.getName() + "][" + data + "]错误[" + e.getMessage() + "]");
        }
    }

    /**
     * 生成节点信息
     *
     * @param id       自己的ID
     * @param parentid 父ID
     * @param name     名称
     * @return
     */
    public Map<String, Object> getTreeNode(String id, String parentid, String name, boolean checked) {
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("id", id);
        nodeMap.put("pId", parentid);
        nodeMap.put("name", name);
        nodeMap.put("open", false);
        nodeMap.put("checked", checked);
        return nodeMap;
    }
}
