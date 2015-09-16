package com.idea.common.cache.core;

import com.google.gson.Gson;
import com.idea.common.cache.ParamCache;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 */
public class ParamConfig implements com.idea.common.cache.support.Config {

    private static Logger logger = Logger.getLogger(ParamConfig.class);  //log4j日志对象

    @Override
    public void init(Element root) {
        if (null == root) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        Element params = root.element("params");
        for (Iterator i = params.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String key = element.attributeValue("key");
            String message = element.attributeValue("value");
            map.put(key, message);
        }
        ParamCache.getInstance().init(map);
        logger.debug("加载Param配置：" + new Gson().toJson(map));
    }
}
