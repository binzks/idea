package com.idea.common.cache;

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
public class JdbcModelCache {

    private Map<String, JdbcModel> cacheMap; //model缓存map

    private static class ConfigureHolder {
        private static JdbcModelCache instance = new JdbcModelCache();
    }

    private JdbcModelCache() {
        this.cacheMap = new HashMap<>();
    }

    public static JdbcModelCache getInstance() {
        return ConfigureHolder.instance;
    }

    /***
     * 清理所有的缓存数据
     */
    public void clear() {
        this.cacheMap.clear();
    }

    /***
     * 初始化model缓存，先清除原先的model
     *
     * @param list
     */
    public void init(List<Model> list) {
        for (Model model : list) {
            String key = model.getName();
            if (null != this.cacheMap.get(key)) {
                throw new RuntimeException("JdbcModel定义[" + key + "]已存在！");
            }
            JdbcModel jdbcModel = ModelFactory.getModel(model);
            this.cacheMap.put(key, jdbcModel);
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
