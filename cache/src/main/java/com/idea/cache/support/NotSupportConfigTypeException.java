package com.idea.cache.support;

/**
 * Created by zhoubin on 15/9/9.
 * 不支持的配置类型异常
 */
public class NotSupportConfigTypeException extends RuntimeException {

    public NotSupportConfigTypeException(String msg) {
        super(msg);
    }
}
