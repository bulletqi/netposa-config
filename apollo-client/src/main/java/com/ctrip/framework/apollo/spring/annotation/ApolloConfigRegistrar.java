package com.ctrip.framework.apollo.spring.annotation;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.utils.NetposaPropertiesUtil;
import com.ctrip.framework.apollo.spring.config.PropertySourcesProcessor;
import com.ctrip.framework.apollo.spring.util.BeanRegistrationUtil;
import com.ctrip.framework.apollo.spring.util.NamespaceUtil;
import com.ctrip.framework.apollo.spring.util.PropertiesContext;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ApolloConfigRegistrar implements ImportBeanDefinitionRegistrar {

	private static final Logger logger = LoggerFactory.getLogger(ApolloConfigRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		try {
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;
			Environment environment = beanFactory.getBean(Environment.class);
			PropertiesContext.setNetposaProperties(environment);
			if (NetposaPropertiesUtil.isEnable()) {
				logger.info("#######开始启用配置中心服务#######");
				AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata
						.getAnnotationAttributes(EnableApolloConfig.class.getName()));
				String[] namespaces = attributes.getStringArray("value");

				List<String> namespaceList;
				if (namespaces.length == 1 && namespaces[0].equals(ConfigConsts.NAMESPACE_APPLICATION)) {
					//如果是默认值，获取所有的namespace
					namespaceList = NamespaceUtil.getAllNamespace();
				} else {
					namespaceList = Lists.newArrayList(namespaces);
				}
				int order = attributes.getNumber("order");
				PropertySourcesProcessor.addNamespaces(namespaceList, order);

				BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, PropertySourcesPlaceholderConfigurer.class.getName(),
						PropertySourcesPlaceholderConfigurer.class);

				BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, PropertySourcesProcessor.class.getName(),
						PropertySourcesProcessor.class);

				BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry, ApolloAnnotationProcessor.class.getName(),
						ApolloAnnotationProcessor.class);
			} else {
				logger.info("#######不启用配置中心服务#######");
			}
		} catch (Exception e) {
			logger.error("配置中心加载namespace失败,不启用配置中心", e);
		}
	}
}
