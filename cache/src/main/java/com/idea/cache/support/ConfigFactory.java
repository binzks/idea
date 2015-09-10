package com.idea.cache.support;

import com.idea.cache.core.ErrorConfig;
import com.idea.cache.core.ParamConfig;
import com.idea.cache.core.TableConfig;
import com.idea.cache.core.ViewConfig;

/**
 * Created by zhoubin on 15/9/9.
 */
public class ConfigFactory {

    /**
     * 获取配置文件实例
     *
     * @param configType 配置类型
     * @return
     */
    public static Config getConfig(ConfigType configType) {
        switch (configType) {
            case View:
                return new ViewConfig();
            case Error:
                return new ErrorConfig();
            case Param:
                return new ParamConfig();
            case Table:
                return new TableConfig();
            default:
                throw new NotSupportConfigTypeException("不支持的配置类型[" + configType + "]");
        }
    }
}
