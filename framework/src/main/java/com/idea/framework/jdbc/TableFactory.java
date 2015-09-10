package com.idea.framework.jdbc;

import com.idea.framework.jdbc.core.table.MysqlTable;
import com.idea.framework.jdbc.support.DataBaseType;
import com.idea.framework.jdbc.support.JdbcTable;
import com.idea.framework.jdbc.support.NotSupportDataBaseTypeException;
import com.idea.framework.jdbc.support.table.Table;

/**
 * Created by zhoubin on 15/9/6.
 * 数据库表工厂，用于生成数据库表处理对象
 */
public class TableFactory {

    /***
     * 获取数据库表处理对象，根据数据库类型获取相应语法的对象，并将表的定义设置到处理JdbcTable对象进行初始化
     *
     * @param dataBaseType 数据库类型
     * @param table        数据库表定义
     * @return 数据库表处理对象
     */
    public static JdbcTable getTable(DataBaseType dataBaseType, Table table) {
        JdbcTable jdbcTable = null;
        if (dataBaseType == DataBaseType.Mysql) {
            jdbcTable = new MysqlTable();
        } else {
            throw new NotSupportDataBaseTypeException("不支持的数据库类型[" + dataBaseType + "]");
        }
        jdbcTable.setTable(table);
        return jdbcTable;
    }
}
