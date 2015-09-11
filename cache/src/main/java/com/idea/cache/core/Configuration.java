package com.idea.cache.core;

import com.idea.cache.support.Config;
import com.idea.cache.support.ConfigFactory;
import com.idea.cache.support.ConfigType;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

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
        try {
            File dir = new File(path);
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.getName().toLowerCase().endsWith(".xml")) {
                    SAXReader reader = new SAXReader();
                    Element root = reader.read(file).getRootElement();
                    String type = root.attributeValue("type");
                    if (StringUtils.isNotBlank(type)) {
                        Config config = ConfigFactory.getConfig(ConfigType.valueOfString(type));
                        if (null != config) {
                            config.init(root);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
