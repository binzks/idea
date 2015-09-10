package com.idea.common.view;

import java.util.List;
import java.util.Map;


public class View {

    private String name; // 名称（全局唯一）
    private String pkName; //对应主表主键名称
    private String title; // 标题
    private Integer rowSize;// 每页行数
    //private Model model;
    private Boolean backupOnDel; // 删除的时候备份数据
    private String workflow; // 工作流

    private List<ViewColumn> columns;
    private List<ViewAction> actions;

    public View(String name, String title, Integer rowSize, Boolean backupOnDel,
                String workflow, List<ViewColumn> columns, List<ViewAction> actions) {
        this.name = name;
        this.title = title;
        this.rowSize = rowSize;
        //this.model = model;
        this.backupOnDel = backupOnDel;
        this.workflow = workflow;
        this.columns = columns;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Integer getRowSize() {
        return rowSize;
    }

    public Boolean getBackupOnDel() {
        return backupOnDel;
    }

    public List<ViewColumn> getColumns() {
        return columns;
    }

    public List<ViewAction> getActions() {
        return actions;
    }

    public String getPkName() {
        return pkName;
    }
}
