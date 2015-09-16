package com.idea.common.cache;

import com.idea.framework.jdbc.TableFactory;
import com.idea.framework.jdbc.support.JdbcTable;
import com.idea.framework.jdbc.support.table.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 * JdbcTabel缓存对象，单例模式，用于创建数据库脚本和分析系统
 */
public class JdbcTableCache {

    private Map<String, JdbcTable> cacheMap; //jdbcTable缓存map

    private static class ConfigureHolder {
        private static JdbcTableCache instance = new JdbcTableCache();
    }

    private JdbcTableCache() {
        this.cacheMap = new HashMap<>();
    }

    public static JdbcTableCache getInstance() {
        return ConfigureHolder.instance;
    }

    /***
     * 初始化table缓存，先清除原先的table
     *
     * @param list
     */
    public void init(List<Table> list) {
        this.cacheMap.clear();
        for (Table table : list) {
            String key = table.getName();
            if (null != this.cacheMap.get(key)) {
                throw new RuntimeException("JdbcTable定义[" + key + "]已存在！");
            }
            JdbcTable jdbcTable = TableFactory.getTable(table);
            jdbcTable.initTable();
            this.cacheMap.put(key, jdbcTable);
        }
    }

    /***
     * 获取一个JdbcTable
     *
     * @param key table的名称
     * @return
     */
    public JdbcTable get(String key) {
        return this.cacheMap.get(key);
    }
}
