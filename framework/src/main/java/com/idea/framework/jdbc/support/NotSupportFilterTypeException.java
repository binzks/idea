package com.idea.framework.jdbc.support;

/**
 * Created by zhoubin on 15/9/7.
 * 不支持的查询过滤类型异常
 */
public class NotSupportFilterTypeException extends RuntimeException {

    public NotSupportFilterTypeException(String msg) {
        super(msg);
    }
}
