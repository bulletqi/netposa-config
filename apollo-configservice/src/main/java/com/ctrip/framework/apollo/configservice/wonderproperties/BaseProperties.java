package com.ctrip.framework.apollo.configservice.wonderproperties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;


@Component
public class BaseProperties{

	private static final Logger logger = LoggerFactory.getLogger(BaseProperties.class);

	private File propertiesFile;

	@Value("file:${apollo.config.home:.}/base.properties")
	private String iniLocation;

	@PostConstruct
	public void init() throws IOException {
		propertiesFile = ResourceUtils.getFile(iniLocation);
		if (!propertiesFile.exists()) {
			logger.error("{}位置找不到base-properties文件", iniLocation);
			propertiesFile = ResourceUtils.getFile("classpath:system/base.properties");
			logger.info("----自定义路径不正确,加载默认配置模板----");
		}
	}

	protected File getPropertiesFile() {
		return this.propertiesFile;
	}

}
