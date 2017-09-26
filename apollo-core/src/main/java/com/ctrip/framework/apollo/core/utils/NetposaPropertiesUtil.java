package com.ctrip.framework.apollo.core.utils;

import java.util.Objects;
import java.util.Properties;

/**
 * 从业务应用的配置文件中读取信息
 */
public class NetposaPropertiesUtil {

//	public final static Properties properties = ResourceUtils.loadNetposaConfigFile();

	public static String getAppId(){
		return NetposaConstant.appId;
	}

	public static String getDevMeta(){
		String localtion = NetposaConstant.devMeta;
		if(localtion != null){
			localtion = "http://" + localtion;
		}
		return  localtion;
	}

	public static boolean isEnable(){
		if(NetposaConstant.isEnable == null ){
			return true;
		}
		return NetposaConstant.isEnable;
	}
}
