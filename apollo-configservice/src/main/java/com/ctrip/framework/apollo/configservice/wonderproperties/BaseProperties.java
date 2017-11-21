package com.ctrip.framework.apollo.configservice.wonderproperties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class BaseProperties extends ResourcePropertySource {

	public BaseProperties() throws IOException {
		super("classpath:/system/base.properties");
	}
}
