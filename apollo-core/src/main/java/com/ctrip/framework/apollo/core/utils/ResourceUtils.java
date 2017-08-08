package com.ctrip.framework.apollo.core.utils;

import com.ctrip.framework.apollo.core.utils.ymal.YamlPropertiesFactoryBean;
import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class ResourceUtils {

	private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);
	private static final String[] DEFAULT_FILE_SEARCH_LOCATIONS = new String[]{"./config/", "./"};

	@SuppressWarnings("unchecked")
	public static Properties readConfigFile(String configPath, Properties defaults) {
		InputStream in = loadConfigFileFromDefaultSearchLocations(configPath);
		logger.debug("Reading config from resource {}", configPath);
		Properties props = new Properties();
		try {
			if (in == null) {
				// load outside resource under current user path
				Path path = new File(System.getProperty("user.dir") + configPath).toPath();
				if (Files.isReadable(path)) {
					in = new FileInputStream(path.toFile());
					logger.debug("Reading config from file {} ", path);
				} else {
					logger.warn("Could not find available config file");
				}
			}
			if (defaults != null) {
				props.putAll(defaults);
			}

			if (in != null) {
				props.load(in);
				in.close();
			}
		} catch (Exception ex) {
			logger.warn("Reading config failed: {}", ex.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					logger.warn("Close config failed: {}", ex.getMessage());
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Enumeration<String> e = (Enumeration<String>) props.propertyNames(); e
				.hasMoreElements(); ) {
			String key = e.nextElement();
			String val = (String) props.getProperty(key);
			sb.append(key).append('=').append(val).append('\n');
		}
		if (sb.length() > 0) {
			logger.debug("Reading properties: \n" + sb.toString());
		} else {
			logger.warn("No available properties");
		}
		return props;
	}

	private static InputStream loadConfigFileFromDefaultSearchLocations(String configPath) {
		for (String searchLocation : DEFAULT_FILE_SEARCH_LOCATIONS) {
			try {
				File candidate = Paths.get(searchLocation, configPath).toFile();
				if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
					return new FileInputStream(candidate);
				}
			} catch (Throwable ex) {
				//ignore
			}
		}

		return ClassLoaderUtil.getLoader().getResourceAsStream(configPath);
	}

	/*
		1.读取业务的配置文件，一般文件名为applicaiton、bootstrap 后缀为yml、properties
	 */
	private static final String[] NETPOSA_CONFIGFILE
			= new String[]{"bootstrap.yml", "bootstrap.properties", "application.yml", "application.properties"};

	public static Properties loadNetposaConfigFile() {
		Properties props = new Properties();
		for (String configPath : NETPOSA_CONFIGFILE) {
			InputStream in = loadConfigFileFromDefaultSearchLocations(configPath);
			logger.debug("Reading config from resource {}", configPath);
			try {
				if (in == null) {
					// load outside resource under current user path
					Path path = new File(System.getProperty("user.dir") +File.separator + configPath).toPath();
					if (Files.isReadable(path)) {
						in = new FileInputStream(path.toFile());
						logger.debug("Reading config from file {} ", path);
					}
				}
				if (in != null) {
					if(configPath.contains("yml")){
						props.putAll(new YamlPropertiesFactoryBean(in).createProperties());
					}else{
						props.load(in);
					}
					in.close();
				}
			} catch (Exception ex) {
				logger.warn("Reading config failed: {}", ex.getMessage());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ex) {
						logger.warn("Close config failed: {}", ex.getMessage());
					}
				}
			}
		}
		return props;
	}

//	private static void getPropertiesfromStream(Properties props) {
//		InputStream in = null;
//		for (String fileName : NETPOSA_CONFIGFILE) {
//			try {
//				in = DefaultApplicationProvider.class.getResourceAsStream("/" + fileName);
//				if (in != null) {
//					props.putAll(new YamlPropertiesFactoryBean(in).createProperties());
//				}
//			} finally {
//				if (in != null) {
//					try {
//						in.close();
//					} catch (IOException ex) {
//						logger.warn("Close Stream failed: {}", ex.getMessage());
//					}
//				}
//			}
//		}
//	}
//
//	private static void getPropertiesfromFile(Properties props) {
//		InputStream in = null;
//		for (String searchLocation : DEFAULT_FILE_SEARCH_LOCATIONS) {
//			for (String fileName : NETPOSA_CONFIGFILE) {
//				File candidate = Paths.get(searchLocation, fileName).toFile();
//				if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
//					try {
//						in = new FileInputStream(candidate);
//						props.putAll(new YamlPropertiesFactoryBean(in).createProperties());
//					} catch (FileNotFoundException e) {
//						logger.error("配置文件加载失败: {}", e.getMessage());
//					} finally {
//						if (in != null) {
//							try {
//								in.close();
//							} catch (IOException ex) {
//								logger.warn("Close Stream failed: {}", ex.getMessage());
//							}
//						}
//					}
//				}
//			}
//		}
//	}
}
