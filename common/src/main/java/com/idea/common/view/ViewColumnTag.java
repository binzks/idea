package com.idea.common.view;

/**
 * Created by zhoubin on 15/9/11.
 * view的列的tag定义
 */
public enum ViewColumnTag {

    Text("TEXT"),
    Date("DATE"),
    DateTime("DATETIME"),
    Password("PASSWORD"),
    Image("IMAGE"),
    Item("ITEM"),
    DateItem("DATAITEM"),
    Int("INT");

    private String name;

    ViewColumnTag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /***
     * 根据字符串获取枚举值
     *
     * @param name 枚举值字符串
     * @return
     */
    public static ViewColumnTag valueOfString(String name) {
        switch (name.toUpperCase()) {
            case "TEXT":
                return ViewColumnTag.Text;
            case "ITEM":
                return ViewColumnTag.Item;
            case "DATAITEM":
                return ViewColumnTag.DateItem;
            case "DATE":
                return ViewColumnTag.Date;
            case "DATETIME":
                return ViewColumnTag.DateTime;
            case "INT":
                return ViewColumnTag.Int;
            case "IMAGE":
                return ViewColumnTag.Image;
            case "PASSWORD":
                return ViewColumnTag.Password;
            default:
                throw new RuntimeException("不支持的数据库类型[" + name + "]");
        }
    }
}
