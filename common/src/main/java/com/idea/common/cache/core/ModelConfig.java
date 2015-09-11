package com.idea.common.cache.core;

import com.google.gson.Gson;
import com.idea.common.cache.ModelCache;
import com.idea.common.cache.support.Config;
import com.idea.framework.jdbc.support.DataBaseType;
import com.idea.framework.jdbc.support.model.*;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Created by zhoubin on 15/9/9.
 */
public class ModelConfig implements Config {

    private static Logger logger = Logger.getLogger(ModelConfig.class);  //log4j日志对象

    @Override
    public void init(Element root) {
        if (null == root) {
            return;
        }
        List<Model> list = new ArrayList<>();
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            List<Join> joins = getJoinList(element.element("joins"));
            List<Filter> filters = getFilterList(element.element("filters"));
            List<Order> orders = getOrderList(element.element("orders"));
            List<Column> columns = getColumns(element.element("columns"));
            Model model = new Model();
            model.setName(element.attributeValue("name"));
            model.setDataBaseType(DataBaseType.valueOfString(element.attributeValue("type")));
            model.setTableName(element.attributeValue("table"));
            model.setJoins(joins);
            model.setFilters(filters);
            model.setOrders(orders);
            model.setColumns(columns);
            list.add(model);
        }
        //初始化所有model转换为JdbcModel
        ModelCache.getInstance().init(list);
        logger.debug("加载model配置：" + new Gson().toJson(list));
    }


    /***
     * 获取列的定义
     *
     * @param columnsElement
     * @return
     */
    private List<Column> getColumns(Element columnsElement) {
        if (null == columnsElement) {
            return null;
        } else {
            List<Column> list = new ArrayList<>();
            for (Iterator i = columnsElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Column column = new Column();
                String name = element.attributeValue("name");
                column.setName(name);
                column.setJoinName(element.attributeValue("joinName"));
                column.setAlias(element.attributeValue("alias"));
                list.add(column);
            }
            return list;
        }
    }

    /***
     * 获取关联定义
     *
     * @param joinsElement
     * @return
     */
    private List<Join> getJoinList(Element joinsElement) {
        if (null == joinsElement) {
            return null;
        } else {
            List<Join> joinList = new ArrayList<>();
            for (Iterator i = joinsElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Join join = new Join();
                join.setName(element.attributeValue("name"));
                join.setTable(element.attributeValue("table"));
                join.setType(element.attributeValue("type"));
                join.setKey(element.attributeValue("key"));
                join.setJoinName(element.attributeValue("joinName"));
                join.setJoinKey(element.attributeValue("joinkey"));
                joinList.add(join);
            }
            return joinList;
        }
    }

    /***
     * 获取过滤定义
     *
     * @param filtersElement
     * @return
     */
    private List<Filter> getFilterList(Element filtersElement) {
        if (null == filtersElement) {
            return null;
        } else {
            List<Filter> filterList = new ArrayList<>();
            for (Iterator i = filtersElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Filter filter = new Filter();
                filter.setJoinName(element.attributeValue("joinName"));
                filter.setKey(element.attributeValue("key"));
                filter.setType(FilterType.valueOfString(element.attributeValue("type")));
                filter.setValue(element.attributeValue("value"));
                filterList.add(filter);
            }
            return filterList;
        }
    }

    /**
     * 获取排序定义
     *
     * @param ordersElement
     * @return
     */
    private List<Order> getOrderList(Element ordersElement) {
        if (null == ordersElement) {
            return null;
        } else {
            List<Order> orderList = new ArrayList<>();
            for (Iterator i = ordersElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Order order = new Order();
                order.setJoinName(element.attributeValue("joinName"));
                order.setKey(element.attributeValue("key"));
                order.setType(OrderType.valueOfString(element.attributeValue("type")));
                orderList.add(order);
            }
            return orderList;
        }
    }
}
