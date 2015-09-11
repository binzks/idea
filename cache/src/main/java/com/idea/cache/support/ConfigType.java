package com.idea.cache.support;

/**
 * Created by zhoubin on 15/9/9.
 * xml配置类型 错误，参数，View，表
 */
public enum ConfigType {
    Error("ERROR"),
    Param("PARAM"),
    Table("TABLE"),
    Business("BUSINESS");

    private String name;

    ConfigType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * 根据字符串获取枚举值
     *
     * @param name 枚举值对应字符串
     * @return
     */
    public static ConfigType valueOfString(String name) {
        switch (name.toUpperCase()) {
            case "BUSINESS":
                return ConfigType.Business;
            case "ERROR":
                return ConfigType.Error;
            case "PARAM":
                return ConfigType.Param;
            case "TABLE":
                return ConfigType.Table;
            default:
                throw new NotSupportConfigTypeException("不支持的配置类型[" + name + "]");
        }
    }
}
