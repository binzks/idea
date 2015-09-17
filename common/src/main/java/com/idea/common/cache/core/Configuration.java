package com.idea.common.cache.core;

import com.idea.common.cache.*;
import com.idea.common.cache.support.Config;
import com.idea.common.cache.support.ConfigFactory;
import com.idea.common.cache.support.ConfigType;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zhoubin on 15/9/9.
 */
public class Configuration {

    /***
     * 读取目录下所有的xml文件，根据xml的配置加载到缓存对象中
     *
     * @param path xml配置文件目录
     */
    public static void init(String path) {
        if (StringUtils.isBlank(path)) {
            return;
        }
        clearCache();
        try {
            File dir = new File(path);
            File[] files = dir.listFiles();
            Map<Element, Config> map = new HashMap<>();
            //如果有table配置，先读取table配置
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".xml")) {
                    SAXReader reader = new SAXReader();
                    Element root = reader.read(file).getRootElement();
                    String type = root.attributeValue("type");
                    if (StringUtils.isNotBlank(type)) {
                        ConfigType configType = ConfigType.valueOfString(type);
                        Config config = ConfigFactory.getConfig(configType);
                        if (ConfigType.Table == configType) {
                            config.init(root);
                        } else {
                            map.put(root, config);
                        }
                    }
                }
            }
            // 加载所有除了table的配置项
            for (Entry<Element, Config> entry : map.entrySet()) {
                entry.getValue().init(entry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 清除所有缓存数据
     */
    public static void clearCache() {
        ErrorCache.getInstance().clear();
        JdbcModelCache.getInstance().clear();
        JdbcTableCache.getInstance().clear();
        ParamCache.getInstance().clear();
        ViewCache.getInstance().clear();
    }


}
