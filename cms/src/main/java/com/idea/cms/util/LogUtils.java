package com.idea.cms.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.idea.common.cache.JdbcModelCache;
import org.apache.log4j.Logger;

public class LogUtils {

    private static Logger logger = Logger.getLogger(LogUtils.class);

    public static void saveCMSLog(String ip, String userId, Object action) {
        Map<String, Object> sysCMSLogMap = new HashMap<>();
        try {
            sysCMSLogMap.put("ip_address", ip);
            sysCMSLogMap.put("user_id", userId);
            sysCMSLogMap.put("create_time", System.currentTimeMillis() / 1000);
            sysCMSLogMap.put("action", action);
            JdbcModelCache.getInstance().get("model_sys_cms_log").insert(sysCMSLogMap);
        } catch (Exception e) {
            logger.error("记录CMS日志[" + new Gson().toJson(sysCMSLogMap) + "]错误[" + e.getMessage() + "]");
        }
    }

}
