package com.ctrip.framework.apollo.core.utils;

/**
 * 从业务应用的配置文件中读取信息
 */
public class NetposaPropertiesUtil {

	public static String getAppId(){
		return ResourceUtils.loadNetposaConfigFile().getProperty("spring.application.name");
	}

	public static String getDevMeta(){
		String localtion = ResourceUtils.loadNetposaConfigFile().getProperty("spring.netposa.center-conf.url");
		return "http://" + localtion;
	}

}
