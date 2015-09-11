package com.idea.framework.jdbc.support.model;

import com.idea.framework.jdbc.support.NotSupportOrderTypeException;

/**
 * Created by zhoubin on 15/9/10.
 * 排序类型
 */
public enum OrderType {

    Asc("ASC"), Desc("DESC");

    private String name;

    OrderType(String name) {
        this.name = name;
    }


    /***
     * 根据字符串获取枚举值
     *
     * @param name 枚举值字符串
     * @return
     */
    public static OrderType valueOfString(String name) {
        switch (name.toUpperCase()) {
            case "ASC":
                return OrderType.Asc;
            case "DESC":
                return OrderType.Desc;
            default:
                throw new NotSupportOrderTypeException("不支持的排序类型[" + name + "]");
        }
    }
}
