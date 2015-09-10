package com.idea.common.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewColumn {

	public enum Tag {
		TEXT, DATE, DATETIME, PASSWORD, IMAGE, ITEM, DATAITEM, INT;
	}

	private String name; // 字段名称
	private String describe; // 字段描述
	private Integer width;
	private Boolean center;
	private String tag;
	private boolean search;
	private boolean display;
	private boolean detail;
	private boolean add;
	private boolean edit;
	private boolean required;
	private boolean rowfilter;
	private String defaultValue;
	private String modelName;
	private String itemKey; // model取值字段
	private String itemValue; // model显示字段
	private Map<String, String> items; // 如果是items类型的item值

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Boolean getCenter() {
		return center;
	}

	public void setCenter(Boolean center) {
		this.center = center;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean isDetail() {
		return detail;
	}

	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isRowfilter() {
		return rowfilter;
	}

	public void setRowfilter(boolean rowfilter) {
		this.rowfilter = rowfilter;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public Map<String, String> getItems() {
		if (tag.toUpperCase().equals(Tag.DATAITEM.toString())) {
//			items = new HashMap<String, String>();
//			List<Map<String, Object>> list = ModelManager.getInstance().getModel(modelName).selectMaps();
//			for (int i = 0; i < list.size(); i++) {
//				Map<String, Object> map = list.get(i);
//				items.put(map.get(itemKey).toString(), map.get(itemValue).toString());
//			}
		}
		return items;
	}

	public void setItems(Map<String, String> items) {
		this.items = items;
	}

}
