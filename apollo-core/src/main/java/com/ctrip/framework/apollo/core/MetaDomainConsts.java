package com.ctrip.framework.apollo.core;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.utils.NetposaPropertiesUtil;
import com.ctrip.framework.apollo.core.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The meta domain will load the meta server from System environment first, if not exist, will load
 * from apollo-env.properties. If neither exists, will load the default meta url.
 * <p>
 * Currently, apollo supports local/dev/fat/uat/lpt/pro environments.
 */
public class MetaDomainConsts {

	private static Map<Env, Object> domains = new HashMap<>();

	private static final Logger logger = LoggerFactory.getLogger(MetaDomainConsts.class);

	public static final String DEFAULT_META_URL = "http://config.local";

//	static {
//		Properties prop = new Properties();
//		prop = ResourceUtils.readConfigFile("apollo-env.properties", prop);
//		Properties env = System.getProperties();
//		domains.put(Env.LOCAL,
//				env.getProperty("local_meta", prop.getProperty("local.meta", DEFAULT_META_URL)));
//		domains.put(Env.DEV,
//				env.getProperty("dev_meta", prop.getProperty("dev.meta", DEFAULT_META_URL)));
//		domains.put(Env.FAT,
//				env.getProperty("fat_meta", prop.getProperty("fat.meta", DEFAULT_META_URL)));
//		domains.put(Env.UAT,
//				env.getProperty("uat_meta", prop.getProperty("uat.meta", DEFAULT_META_URL)));
//		domains.put(Env.LPT,
//				env.getProperty("lpt_meta", prop.getProperty("lpt.meta", DEFAULT_META_URL)));
//		domains.put(Env.PRO,
//				env.getProperty("pro_meta", prop.getProperty("pro.meta", DEFAULT_META_URL)));
//	}

//	public static String getDomain(Env env) {
//		return String.valueOf(domains.get(env));
//	}

	/*
		1.从新的配置文件上读取信息
		2.默认dev环境
	 */
	static {
		Properties prop = new Properties();
		prop = ResourceUtils.readConfigFile("netposa-centerconf.properties", prop);
		Properties env = System.getProperties();
		domains.put(Env.DEV,
				env.getProperty("dev_meta", prop.getProperty("server.url", DEFAULT_META_URL)));
	}

	//metaService的地址
	public static String getDomain(Env env) {
		String devMeta = NetposaPropertiesUtil.getDevMeta();
		devMeta = (devMeta == null ? String.valueOf(domains.get(env)) : devMeta);
//		logger.info("配置中心服务地址 {} ", devMeta);
		return devMeta;
	}

	public static String getDevMeta() {
		String devMeta = NetposaPropertiesUtil.getDevMeta();
		devMeta = (devMeta == null ? String.valueOf(domains.get(Env.DEV)) : devMeta);
		return devMeta;
	}
}
