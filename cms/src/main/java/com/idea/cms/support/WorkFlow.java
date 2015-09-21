package com.idea.cms.support;

import java.util.Map;

/**
 * Created by zhoubin on 15/9/16.
 * 工作流
 */
public class WorkFlow {

    private String id;  //工作流id
    private String field;  //工作流对应的数据的状态字段名称
    private String userField; //工作流对应的数据的修改人字段
    private String timeField; //工作流对应的数据的修改时间字段
    private Map<String, WorkFlowStep> stepMap;  //有权限的工作流节点，string填入节点对应的状态字段值

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField;
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    public Map<String, WorkFlowStep> getStepMap() {
        return stepMap;
    }

    public void setStepMap(Map<String, WorkFlowStep> stepMap) {
        this.stepMap = stepMap;
    }

    public WorkFlowStep getWorkFlowStep(String key) {
        return this.stepMap.get(key);
    }
}
