package com.idea.cache;

import com.idea.framework.jdbc.ModelFactory;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.model.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 * model的数据缓存，单例模式
 */
public class ModelCache {

    private Map<String, JdbcModel> cacheMap; //model缓存map

    private static class ConfigureHolder {
        private static ModelCache instance = new ModelCache();
    }

    private ModelCache() {
        this.cacheMap = new HashMap<>();
    }

    public static ModelCache getInstance() {
        return ConfigureHolder.instance;
    }

    /***
     * 初始化model缓存，先清除原先的model
     *
     * @param list
     */
    public void init(List<Model> list) {
        this.cacheMap.clear();
        for (Model model : list) {
            JdbcModel jdbcModel = ModelFactory.getModel(model);
            this.cacheMap.put(model.getName(), jdbcModel);
        }
    }

    /***
     * 获取一个model
     *
     * @param key model的名称
     * @return
     */
    public JdbcModel get(String key) {
        return this.cacheMap.get(key);
    }

}
