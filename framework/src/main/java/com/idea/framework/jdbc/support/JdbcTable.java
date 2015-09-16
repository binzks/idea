package com.idea.framework.jdbc.support;

import com.idea.framework.jdbc.support.table.Table;

/**
 * Created by zhoubin on 15/9/6.
 * jdbc数据表接口
 */
public interface JdbcTable {

    /***
     * 设置表的定义信息
     *
     * @param table 表定义
     */
    void setTable(Table table);

    /***
     * 初始化数据库表，如果表不存在则创建表、添加索引、添加初始化数据，如果表存在则修改表字段
     */
    void initTable();
}
