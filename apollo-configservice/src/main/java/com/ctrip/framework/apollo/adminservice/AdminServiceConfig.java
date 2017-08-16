package com.ctrip.framework.apollo.adminservice;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = AdminServiceConfig.class)
public class AdminServiceConfig {

}
