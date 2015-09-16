package com.idea.common.cache.core;

import com.google.gson.Gson;
import com.idea.common.cache.ErrorCache;
import com.idea.common.cache.support.Config;
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
        Map<String, String> map = new HashMap<>();
        Element errors = root.element("errors");
        for (Iterator i = errors.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String code = element.attributeValue("code");
            String message = element.attributeValue("message");
            map.put(code, message);
        }
        ErrorCache.getInstance().init(map);
        logger.debug("加载Error配置：" + new Gson().toJson(map));
    }
}
