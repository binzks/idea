package com.idea.cms.support;

/**
 * Created by zhoubin on 15/9/17.
 * 工作流节点
 */
public class WorkFlowStep {
    private String name;  //节点名称
    private String value;  //节点的值
    private Integer type;  //节点类型 0-起点 1-终点 2-节点
    private String nextValue; //下一个节点的值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNextValue() {
        return nextValue;
    }

    public void setNextValue(String nextValue) {
        this.nextValue = nextValue;
    }
}
