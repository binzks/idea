package com.idea.cache.core;

import com.google.gson.Gson;
import com.idea.cache.ErrorCache;
import com.idea.cache.support.Config;
import com.idea.cache.support.RepeatDefinitionException;
import com.sun.javafx.collections.MappingChange;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 */
public class ErrorConfig implements Config {


    private static Logger logger = Logger.getLogger(ErrorConfig.class);  //log4j日志对象

    @Override
    public void init(Element root) {
        if (null == root) {
            return;
        }
        ErrorCache errorCache = ErrorCache.getInstance();
        Map<String, String> map = new HashMap<>();
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String code = element.attributeValue("code");
            if (null != errorCache.get(code)) {
                throw new RepeatDefinitionException("error[" + code + "]已存在重复名称");
            }
            String message = element.attributeValue("message");
            map.put(code, message);
        }
        errorCache.init(map);
        logger.debug("加载Error配置：" + new Gson().toJson(map));
    }
}
