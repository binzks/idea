package com.idea.cache.core;

import com.google.gson.Gson;
import com.idea.cache.ParamCache;
import com.idea.cache.support.Config;
import com.idea.cache.support.RepeatDefinitionException;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 */
public class ParamConfig implements Config {

    private static Logger logger = Logger.getLogger(ParamConfig.class);  //log4j日志对象

    @Override
    public void init(Element root) {
        if (null == root) {
            return;
        }
        ParamCache paramCache = ParamCache.getInstance();
        Map<String, String> map = new HashMap<>();
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String code = element.attributeValue("code");
            if (null != paramCache.get(code)) {
                throw new RepeatDefinitionException("param[" + code + "]已存在重复名称");
            }
            String message = element.attributeValue("message");
            map.put(code, message);
        }
        paramCache.init(map);
        logger.debug("加载Param配置：" + new Gson().toJson(map));
    }
}
