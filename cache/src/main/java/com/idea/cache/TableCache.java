package com.idea.cache;

import com.idea.framework.jdbc.TableFactory;
import com.idea.framework.jdbc.support.DataBaseType;
import com.idea.framework.jdbc.support.JdbcTable;
import com.idea.framework.jdbc.support.NotSupportDataBaseTypeException;
import com.idea.framework.jdbc.support.table.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/9.
 * JdbcTabel缓存对象，单例模式，用于创建数据库脚本和分析系统
 */
public class TableCache {

    private Map<String, JdbcTable> cacheMap; //jdbcTable缓存map

    private static class ConfigureHolder {
        private static TableCache instance = new TableCache();
    }

    private TableCache() {
        this.cacheMap = new HashMap<>();
    }

    public static TableCache getInstance() {
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
            JdbcTable jdbcTable = TableFactory.getTable(table);
            this.cacheMap.put(table.getName(), jdbcTable);
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
