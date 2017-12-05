package com.ctrip.framework.apollo.configservice.wonderproperties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Component
public class BaseProperties{

	private static final Logger logger = LoggerFactory.getLogger(BaseProperties.class);

	private ResourcePropertySource resourcePropertySource;

	@Value("${base.properties.location}")
	private String iniLocation;

	@PostConstruct
	public void init() throws IOException {
		try{
			resourcePropertySource = new ResourcePropertySource(iniLocation);
		}catch (IOException e){
			logger.error("无法加载base-properties,{}",e.getMessage());
			resourcePropertySource =
					new ResourcePropertySource("classpath:/system/base.properties");
			logger.info("----自定义路径不正确,加载默认配置模板----");
		}
	}

	public  ResourcePropertySource getResourcePropertySource(){
		return this.resourcePropertySource;
	}

}
