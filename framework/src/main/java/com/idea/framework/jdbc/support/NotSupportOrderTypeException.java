package com.idea.framework.jdbc.support;

/**
 * Created by zhoubin on 15/9/7.
 * 不支持的排序类型异常
 */
public class NotSupportOrderTypeException extends RuntimeException {

    public NotSupportOrderTypeException(String msg) {
        super(msg);
    }
}
