package com.ctrip.framework.apollo.core.utils;

import java.util.Objects;
import java.util.Properties;

/**
 * 从业务应用的配置文件中读取信息
 */
public class NetposaPropertiesUtil {

	public final static Properties properties = ResourceUtils.loadNetposaConfigFile();


	public static String getAppId(){
		String appId = NetposaConstant.appId;
		Objects.requireNonNull(appId,"配置中心应用id为空");
		return appId;
	}

	public static String getDevMeta(){
		String localtion = NetposaConstant.devMeta;
		Objects.requireNonNull(localtion,"配置中心应用服务地址为空");
		return  "http://" + localtion;
	}

	public static Boolean isEnable(){
		if(NetposaConstant.isEnable == null ){
			return false;
		}
		return NetposaConstant.isEnable;
	}
}
