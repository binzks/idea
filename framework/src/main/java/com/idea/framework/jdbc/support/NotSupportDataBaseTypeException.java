package com.idea.framework.jdbc.support;

/**
 * Created by zhoubin on 15/9/7.
 * 不支持的数据库类型异常
 */
public class NotSupportDataBaseTypeException extends RuntimeException {

    public NotSupportDataBaseTypeException(String msg) {
        super(msg);
    }
}
