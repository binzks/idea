package com.idea.cache.core;

import com.google.gson.Gson;
import com.idea.cache.TableCache;
import com.idea.cache.support.Config;
import com.idea.cache.support.RepeatDefinitionException;
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
        TableCache tableCache = TableCache.getInstance();
        List<Table> list = new ArrayList<>();
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            List<Field> fields = getFields(element.element("fields"));
            List<Index> indexes = getIndexes(element.element("indexes"));
            List<Object[]> data = getData(element.element("datas"), fields);
            Table table = new Table();
            table.setDataBaseType(DataBaseType.valueOfString(element.attributeValue("type").toUpperCase()));
            String name = element.attributeValue("name");
            table.setName(name);
            table.setComment(element.attributeValue("comment"));
            table.setDsName(element.attributeValue("ds"));
            table.setPkName(element.attributeValue("pk"));
            table.setFields(fields);
            table.setIndexes(indexes);
            table.setData(data);
            if (null != tableCache.get(name)) {
                throw new RepeatDefinitionException("table[" + name + "]已存在重复名称");
            }
            list.add(table);
        }
        tableCache.init(list);
        logger.debug("加载table配置：" + new Gson().toJson(list));
    }

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
