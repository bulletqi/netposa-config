package com.ctrip.framework.apollo.core.utils;

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
	private static final String[] NETPOSA_CONFIGFILE = new String[]{"bootstrap.yml", "bootstrap.properties","application.yml", "application.properties"};
	public static Map loadNetposaConfigFile() {
		Map props = Collections.EMPTY_MAP;
		Yaml yaml = new Yaml();
		Properties aa = new Properties();
		InputStream in = null;
		try {
			//1.从文件里读取
			in = getPropertiesfromFile();
			//2.从classpath中读取
			if (in == null) {
				in = getPropertiesfromStream();
			}
			if(in != null){
				props = (Map) yaml.load(in);
				aa.load(in);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					logger.warn("Close config failed: {}", ex.getMessage());
				}
			}
		}
		return props;
	}

	private static InputStream getPropertiesfromStream(){
		InputStream in = null;
		for (String fileName : NETPOSA_CONFIGFILE) {
			in = DefaultApplicationProvider.class.getResourceAsStream("/" + fileName);
			if(in != null){
				break;
			}
		}
		return in;
	}

	private static InputStream getPropertiesfromFile(){
		for (String searchLocation : DEFAULT_FILE_SEARCH_LOCATIONS) {
			for (String fileName : NETPOSA_CONFIGFILE) {
				File candidate = Paths.get(searchLocation, fileName).toFile();
				if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
					try {
						return new FileInputStream(candidate);
					} catch (FileNotFoundException e) {
						logger.error("配置文件加载失败: {}", e.getMessage());
					}
				}
			}
		}
		return null;
	}

}
