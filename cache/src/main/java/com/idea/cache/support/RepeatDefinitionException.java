package com.idea.cache.support;

/**
 * Created by zhoubin on 15/9/10.
 * 重复定义的异常
 */
public class RepeatDefinitionException extends RuntimeException {

    public RepeatDefinitionException(String msg) {
        super(msg);
    }
}
