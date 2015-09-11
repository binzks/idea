package com.idea.framework.jdbc;

import com.idea.framework.jdbc.core.model.MysqlModel;
import com.idea.framework.jdbc.support.DataBaseType;
import com.idea.framework.jdbc.support.JdbcModel;
import com.idea.framework.jdbc.support.NotSupportDataBaseTypeException;
import com.idea.framework.jdbc.support.model.Model;

/**
 * Created by zhoubin on 15/9/7.
 * model工厂，用于获取model对象
 */
public class ModelFactory {

    /***
     * 获取数据model对象，根据数据库类型获取对应对象，用于数据增、删、改、查
     *
     * @param model 数据model定义
     * @return 数据model对象
     */
    public static JdbcModel getModel(Model model) {
        JdbcModel jdbcModel = null;
        if (model.getDataBaseType() == DataBaseType.Mysql) {
            jdbcModel = new MysqlModel();
        } else {
            throw new NotSupportDataBaseTypeException("不支持的数据库类型[" + model.getDataBaseType().toString() + "]");
        }
        jdbcModel.setModel(model);
        return jdbcModel;
    }
}
