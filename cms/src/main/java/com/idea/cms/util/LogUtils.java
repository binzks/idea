package com.idea.cms.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class LogUtils {

	private static Logger logger = Logger.getLogger(LogUtils.class);

	public static void saveCMSLog(String ip, String userId, Object action) {
		try {
			Map<String, Object> sysCMSLogMap = new HashMap<String, Object>();
			sysCMSLogMap.put("ipaddr", ip);
			sysCMSLogMap.put("userid", userId);
			sysCMSLogMap.put("createtime", System.currentTimeMillis() / 1000);
			sysCMSLogMap.put("action", action);
			//Think.getModel("model_sys_cms_log").insert(sysCMSLogMap);
		} catch (Exception e) {
			logger.error("记录CMS日志[" + userId + "][" + ip + "][" + action + "]错误[" + e.getMessage() + "]");
		}
	}

}
