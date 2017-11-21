package com.ctrip.framework.apollo.configservice.wonderproperties;


import com.ctrip.framework.apollo.configservice.SystemPropertiesInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ViasINI{

	private static final Logger logger = LoggerFactory.getLogger(ViasINI.class);

	private ResourcePropertySource resourcePropertySource;

	@Value("${vias.ini.location}")
	private String iniLocation;

	/**
	 * 读取配置项
	 */
	public void loadViasINI() throws IOException {
		try{
			resourcePropertySource = new ResourcePropertySource(iniLocation);
		}catch (IOException e){
			logger.error("无法加载初始化配置项模板",e);
			//加载默认配置
			resourcePropertySource =
					new ResourcePropertySource("classpath:/system/vias.ini");
			logger.info("######加载默认配置模板######");
		}
	}

	public  ResourcePropertySource getVaisINI(){
		return this.resourcePropertySource;
	}

}
