package com.idea.cache;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 * 参数缓存，单例模块
 */
public class ParamCache {

    private Map<String, String> cacheMap; //param缓存map

    private static class ConfigureHolder {
        private static ParamCache instance = new ParamCache();
    }

    private ParamCache() {
        this.cacheMap = new HashMap<>();
    }

    public static ParamCache getInstance() {
        return ConfigureHolder.instance;
    }

    /**
     * 初始化参数定义缓存
     *
     * @param map
     */
    public void init(Map<String, String> map) {
        this.cacheMap.clear();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.cacheMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取参数值，如果参数带有${key}这样的设置，则获取System.getProperty(key)值替换
     *
     * @param key 参数名称
     * @return
     */
    public String get(String key) {
        String result = this.cacheMap.get(key);
        if (null != result && result.contains("${")) {
            String k = StringUtils.substringBetween(result, "${", "}");
            String v = System.getProperty(k);
            if (null == v) {
                v = "";
            }
            result = result.replace("${" + k + "}", v);
        }
        return result;
    }


}
