package com.ctrip.framework.apollo.assembly;

import com.ctrip.framework.apollo.configservice.ConfigServiceApplication;
import com.ctrip.framework.apollo.portal.PortalApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
public class ApolloApplication {

	private static final Logger logger = LoggerFactory.getLogger(ApolloApplication.class);

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext commonContext =
				new SpringApplicationBuilder(ApolloApplication.class).web(false).run(args);
		commonContext.addApplicationListener(new ApplicationPidFileWriter());

		//启动configservice
		ConfigurableApplicationContext configContext =
				new SpringApplicationBuilder(ConfigServiceApplication.class).parent(commonContext)
						.sources(RefreshScope.class).profiles("config").bannerMode(Banner.Mode.OFF).run(args);

		//启动portalservice
		ConfigurableApplicationContext portalContext =
				new SpringApplicationBuilder(PortalApplication.class).parent(commonContext)
						.sources(RefreshScope.class).profiles("portal").bannerMode(Banner.Mode.OFF).run(args);

		logger.info("统一配置中心启动完毕({} isActive:{},{} isActive:{})",configContext.getId(),
				commonContext.isActive(),portalContext.getId(),portalContext.isActive());
	}

}
