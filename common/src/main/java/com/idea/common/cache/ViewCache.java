package com.idea.common.cache;

import com.idea.common.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 * View的缓存，单例模式
 */
public class ViewCache {

    private Map<String, View> cacheMap; //view缓存map

    private static class ConfigureHolder {
        private static ViewCache instance = new ViewCache();
    }

    private ViewCache() {
        this.cacheMap = new HashMap<>();
    }

    public static ViewCache getInstance() {
        return ConfigureHolder.instance;
    }

    /***
     * 初始化view缓存，先清除原先的view
     *
     * @param list
     */
    public void init(List<View> list) {
        this.cacheMap.clear();
        for (View view : list) {
            this.cacheMap.put(view.getName(), view);
        }
    }

    /***
     * 获取一个view
     *
     * @param key view的名称
     * @return
     */
    public View get(String key) {
        return this.cacheMap.get(key);
    }
}
