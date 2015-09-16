package com.idea.common.cache.core;

import com.google.gson.Gson;
import com.idea.common.cache.JdbcTableCache;
import com.idea.common.cache.support.Config;
import com.idea.framework.jdbc.support.DataBaseType;
import com.idea.framework.jdbc.support.table.Field;
import com.idea.framework.jdbc.support.table.Index;
import com.idea.framework.jdbc.support.table.Table;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhoubin on 15/9/9.
 * table配置
 */
public class TableConfig implements Config {

    private static Logger logger = Logger.getLogger(TableConfig.class);  //log4j日志对象

    @Override
    public void init(Element root) {
        if (null == root) {
            return;
        }
        List<Table> list = new ArrayList<>();
        Element tables = root.element("tables");
        for (Iterator i = tables.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String name = element.attributeValue("name");
            List<Field> fields = getFields(element.element("fields"));
            List<Index> indexes = getIndexes(element.element("indexes"));
            List<Object[]> data = getData(element.element("datas"), fields);
            Table table = new Table();
            table.setDataBaseType(DataBaseType.valueOfString(element.attributeValue("type")));
            table.setName(name);
            table.setComment(element.attributeValue("comment"));
            table.setDsName(element.attributeValue("ds"));
            table.setPkName(element.attributeValue("pk"));
            table.setFields(fields);
            table.setIndexes(indexes);
            table.setData(data);
            list.add(table);
        }
        JdbcTableCache.getInstance().init(list);
        logger.debug("加载table配置：" + new Gson().toJson(list));
    }

    /***
     * 获取table的字段定义
     *
     * @param fieldsElement
     * @return
     */
    private List<Field> getFields(Element fieldsElement) {
        if (null == fieldsElement) {
            return null;
        }
        List<Field> list = new ArrayList<>();
        for (Iterator i = fieldsElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            Field field = new Field();
            String name = element.attributeValue("name");
            field.setName(name);
            field.setComment(element.attributeValue("comment"));
            String type = element.attributeValue("type");
            if (StringUtils.isBlank(type)) {
                type = "varchar";
            }
            field.setType(type);
            field.setSize(element.attributeValue("size"));
            String isNull = element.attributeValue("isnull");
            if (StringUtils.isNotBlank(isNull) && isNull.toLowerCase().equals(Boolean.FALSE.toString())) {
                field.setIsNull(false);
            } else {
                field.setIsNull(true);
            }
            field.setDefaultValue(element.attributeValue("default"));
            list.add(field);
        }
        return list;
    }

    /***
     * 获取table的索引定义
     *
     * @param indexesElement
     * @return
     */
    private List<Index> getIndexes(Element indexesElement) {
        if (null == indexesElement) {
            return null;
        }
        List<Index> list = new ArrayList<>();
        for (Iterator i = indexesElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            Index index = new Index();
            String name = element.attributeValue("name");
            index.setName(name);
            index.setComment(element.attributeValue("comment"));
            String type = element.attributeValue("type");
            if (StringUtils.isBlank(type)) {
                type = "UNIQUE";
            }
            index.setType(type);
            index.setFields(element.attributeValue("fields"));
            list.add(index);
        }
        return list;
    }

    /***
     * 获取table的默认初始化表数据
     *
     * @param dataElement
     * @param fields
     * @return
     */
    private List<Object[]> getData(Element dataElement, List<Field> fields) {
        if (null == dataElement || null == fields) {
            return null;
        }
        List<Object[]> list = new ArrayList<>();
        for (Iterator i = dataElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            List<Object> data = new ArrayList<>();
            for (Field field : fields) {
                data.add(element.attributeValue(field.getName()));
            }
            list.add(data.toArray());
        }
        return list;
    }
}
