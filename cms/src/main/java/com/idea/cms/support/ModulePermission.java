package com.idea.cms.support;

import com.idea.common.view.View;

import java.util.Map;


public class ModulePermission {

	private String modelName; // 模块名称
	private View view; // 模块对应的view
	private Map<String, String> actions; // 用户模块的按钮权限，有值的说明没有权限
	private Map<String, String> columns; // 用户模块的列权限，有值的说明没有权限
	private Map<String, String> rowFilters; // 用户模块对应的行级权限，有值的说明有权限
	private String workFlowField;// 工作流对应状态字段名称
	private String workFlowUserField; // 工作流修改用户字段名称
	private String workFlowTimeField; // 工作流修改时间字段名称
	private String workFlowValues; // 有权限工作流节点值
	private Map<String, String> workFlowStep; // 工作流流程步骤，工作流对应的value值和工作流节点的类型（0-起点
												// 1-过程点 2-终点）map

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

	public String getWorkFlowField() {
		return workFlowField;
	}

	public void setWorkFlowField(String workFlowField) {
		this.workFlowField = workFlowField;
	}

	public String getWorkFlowUserField() {
		return workFlowUserField;
	}

	public void setWorkFlowUserField(String workFlowUserField) {
		this.workFlowUserField = workFlowUserField;
	}

	public String getWorkFlowTimeField() {
		return workFlowTimeField;
	}

	public void setWorkFlowTimeField(String workFlowTimeField) {
		this.workFlowTimeField = workFlowTimeField;
	}

	public String getWorkFlowValues() {
		return workFlowValues;
	}

	public void setWorkFlowValues(String workFlowValues) {
		this.workFlowValues = workFlowValues;
	}

	public Map<String, String> getWorkFlowStep() {
		return workFlowStep;
	}

	public void setWorkFlowStep(Map<String, String> workFlowStep) {
		this.workFlowStep = workFlowStep;
	}

}
