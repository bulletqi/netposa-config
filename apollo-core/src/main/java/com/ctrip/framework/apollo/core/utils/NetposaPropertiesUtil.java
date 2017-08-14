package com.ctrip.framework.apollo.core.utils;

import java.util.Properties;

/**
 * 从业务应用的配置文件中读取信息
 */
public class NetposaPropertiesUtil {

	public final static Properties properties = ResourceUtils.loadNetposaConfigFile();


	public static String getAppId(){
		return properties.getProperty("spring.application.name");
	}

	public static String getDevMeta(){
		String localtion = properties.getProperty("spring.netposa.center-conf.url");
		if(localtion != null){
			localtion = "http://" + localtion;
		}
		return  localtion;
	}

	public static Boolean isEnableCenterConf(){
		Boolean property = (Boolean)properties.get("spring.netposa.center-conf.enable");
		return property;
	}

}
