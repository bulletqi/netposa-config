package com.ctrip.framework.apollo.core.utils;

/**
 * 从业务应用的配置文件中读取信息
 */
public class NetposaPropertiesUtil {

	public static String getAppId(){
		return (String) ResourceUtils.loadNetposaConfigFile().get("spring.application.name");
	}

	public static String getDevMeta(){
		String localtion = (String) ResourceUtils.loadNetposaConfigFile().get("spring.netposa.center-conf.url");
		return "http://" + localtion;
	}

}
