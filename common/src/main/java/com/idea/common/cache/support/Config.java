package com.idea.common.cache.support;

import org.dom4j.Element;

/**
 * Created by zhoubin on 15/9/9.
 */
public interface Config {

    /***
     * 根据xml初始化配置文件，并将配置缓存
     *
     * @param root
     */
    void init(Element root);
}
