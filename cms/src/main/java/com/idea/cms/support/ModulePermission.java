package com.idea.cms.support;

import com.idea.common.view.View;
import com.idea.framework.jdbc.support.JdbcModel;

import java.util.Map;


public class ModulePermission {

    private String modelName; // 模块名称
    private View view; // 模块对应的view
    private JdbcModel jdbcModel; //模块对应的数据处理model
    private String pkName;  //模块对应的主键名称
    private Map<String, String> actions; // 用户模块的按钮权限，有值的说明没有权限
    private Map<String, String> columns; // 用户模块的列权限，有值的说明没有权限
    private Map<String, String> rowFilters; // 用户模块对应的行级权限，有值的说明有权限
    private WorkFlow workFlow;  //模块对应的工作流权限

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public JdbcModel getJdbcModel() {
        return jdbcModel;
    }

    public void setJdbcModel(JdbcModel jdbcModel) {
        this.jdbcModel = jdbcModel;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public Map<String, String> getActions() {
        return actions;
    }

    public void setActions(Map<String, String> actions) {
        this.actions = actions;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public Map<String, String> getRowFilters() {
        return rowFilters;
    }

    public void setRowFilters(Map<String, String> rowFilters) {
        this.rowFilters = rowFilters;
    }

    public WorkFlow getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(WorkFlow workFlow) {
        this.workFlow = workFlow;
    }
}
