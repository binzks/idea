package com.idea.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zhoubin on 15/9/9.
 * 错误信息缓存，单例模式
 */
public class ErrorCache {

    private Map<String, String> cacheMap; //error缓存map

    private static class ConfigureHolder {
        private static ErrorCache instance = new ErrorCache();
    }

    private ErrorCache() {
        this.cacheMap = new HashMap<>();
    }

    public static ErrorCache getInstance() {
        return ConfigureHolder.instance;
    }

    /**
     * 初始化错误定义缓存
     *
     * @param map
     */
    public void init(Map<String, String> map) {
        this.cacheMap.clear();
        for (Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (null != this.cacheMap.get(key)) {
                throw new RuntimeException("错误定义[" + key + "]已存在！");
            }
            this.cacheMap.put(key, entry.getValue());
        }
    }

    /**
     * 获取错误定义
     *
     * @param key 错误编号
     * @return
     */
    public String get(String key) {
        return this.cacheMap.get(key);
    }

    /***
     * 根据参数和错误编号生成错误信息
     *
     * @param key   错误编号
     * @param param 参数
     * @return
     */
    public String get(String key, String param) {
        String msg = this.cacheMap.get(key);
        if (null != msg) {
            if (null == param) {
                return msg.replace("?", "");
            } else {
                return msg.replace("?", param);
            }
        } else {
            return null;
        }
    }

}
