package com.idea.common.cache.core;

import com.idea.common.cache.support.Config;
import com.idea.common.view.ViewAction;
import com.idea.common.view.ViewColumn;
import com.idea.common.view.ViewColumnTag;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhoubin on 15/9/11.
 */
public class ViewConfig implements Config {
    @Override
    public void init(Element root) {
        if (null == root) {
            return;
        }
//        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
//            Element element = (Element) i.next();
//            String name = element.attributeValue("name");
//            String title = element.attributeValue("title");
//            Integer rowSize = getIntFromString(element.attributeValue("rowsize"), 10);
//            Model model = ModelManager.getInstance().getModel(element.attributeValue("model"));
//            Boolean backupOnDel = getBoolFromString(element.attributeValue("backupondel"), true);
//            String workflow = element.attributeValue("workflow");
//            List<ViewColumn> columns = getViewColumns(element.element("columns"));
//            List<ViewAction> actions = getViewActions(element.element("actions"));
//            View view = new View(name, title, rowSize, model, backupOnDel, workflow, columns, actions);
//            this.viewMap.put(name, view);
//            Gson gson = new Gson();
//            logger.debug("加载view[" + name + "][" + title + "][rowSize=" + rowSize + "][backupOnDel=" + backupOnDel
//                    + "]workflow[" + workflow + "]columns=" + gson.toJson(columns) + "actions=" + gson.toJson(actions));
//        }
    }

    private List<ViewColumn> getViewColumns(Element columnsElement) {
        if (null == columnsElement) {
            return null;
        }
        List<ViewColumn> list = new ArrayList<>();
        for (Iterator i = columnsElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            ViewColumn viewColumn = new ViewColumn();
            viewColumn.setName(element.attributeValue("name"));
            viewColumn.setDescribe(element.attributeValue("describe"));
            viewColumn.setWidth(getIntFromString(element.attributeValue("width"), 0));
            viewColumn.setCenter(getBoolFromString(element.attributeValue("center"), true));
            String tag = element.attributeValue("tag");
            if (StringUtils.isBlank(tag)) {
                tag = "text";
            }
            viewColumn.setTag(ViewColumnTag.valueOfString(tag));
            viewColumn.setSearch(getBoolFromString(element.attributeValue("search"), false));
            viewColumn.setDisplay(getBoolFromString(element.attributeValue("display"), true));
            viewColumn.setDetail(getBoolFromString(element.attributeValue("detail"), true));
            viewColumn.setAdd(getBoolFromString(element.attributeValue("add"), true));
            viewColumn.setEdit(getBoolFromString(element.attributeValue("edit"), true));
            viewColumn.setRequired(getBoolFromString(element.attributeValue("required"), false));
            viewColumn.setRowFilter(getBoolFromString(element.attributeValue("rowFilter"), false));
            viewColumn.setDefaultValue(element.attributeValue("default"));
            viewColumn.setItems(getItems(element.element("items")));
            Element dataItemElement = element.element("dataitem");
            if (null != dataItemElement) {
                viewColumn.setItemModel(dataItemElement.attributeValue("model"));
                viewColumn.setItemKey(dataItemElement.attributeValue("key"));
                viewColumn.setItemValue(dataItemElement.attributeValue("value"));
            }
            list.add(viewColumn);
        }
        return list;
    }

    private List<ViewAction> getViewActions(Element actionsElement) {
        if (null == actionsElement) {
            return null;
        }
        List<ViewAction> list = new ArrayList<>();
        for (Iterator i = actionsElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            ViewAction viewAction = new ViewAction();
            viewAction.setName(element.attributeValue("name"));
            viewAction.setDescribe(element.attributeValue("describe"));
            viewAction.setType(element.attributeValue("type"));
            viewAction.setHref(element.attributeValue("href"));
            viewAction.setCss(element.attributeValue("css"));
            viewAction.setConfirm(element.attributeValue("confirm"));
            list.add(viewAction);
        }
        return list;
    }

    private Map<String, String> getItems(Element itemsElement) {
        if (null == itemsElement) {
            return null;
        }
        Map<String, String> items = new LinkedHashMap<String, String>();
        for (Iterator item = itemsElement.elementIterator(); item.hasNext(); ) {
            Element itemElement = (Element) item.next();
            items.put(itemElement.attributeValue("value"), itemElement.attributeValue("name"));
        }
        return items;
    }

    private boolean getBoolFromString(String value, boolean defaultValue) {
        boolean result = defaultValue;
        if (null != value) {
            if (value.toLowerCase().equals(Boolean.FALSE.toString())) {
                result = false;
            } else {
                result = true;
            }
        }
        return result;
    }

    private Integer getIntFromString(String width, int defaultValue) {
        Integer result = defaultValue;
        if (StringUtils.isNotBlank(width) && StringUtils.isNumeric(width)) {
            result = Integer.valueOf(width);
        }
        return result;
    }
}
