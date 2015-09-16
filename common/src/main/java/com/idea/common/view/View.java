package com.idea.common.view;

import com.idea.framework.jdbc.support.JdbcModel;

import java.util.List;


public class View {

    private String name; // 名称（全局唯一）
    private String modelName; //model的名称
    private String title; // 标题
    private Integer rowSize;// 每页行数
    private List<ViewColumn> columns;  //view的列
    private List<ViewAction> actions;  //view的操作

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRowSize() {
        return rowSize;
    }

    public void setRowSize(Integer rowSize) {
        this.rowSize = rowSize;
    }

    public List<ViewColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ViewColumn> columns) {
        this.columns = columns;
    }

    public List<ViewAction> getActions() {
        return actions;
    }

    public void setActions(List<ViewAction> actions) {
        this.actions = actions;
    }

}
