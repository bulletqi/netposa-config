package com.ctrip.framework.apollo.assembly;

import com.ctrip.framework.apollo.ConfigServiceApplication;
import com.ctrip.framework.apollo.portal.PortalApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /*
      Common
     */
    ConfigurableApplicationContext commonContext =
        new SpringApplicationBuilder(ApolloApplication.class).web(false).run(args);
    commonContext.addApplicationListener(new ApplicationPidFileWriter());
    logger.info(commonContext.getId() + " #### isActive: " + commonContext.isActive());

    //启动configservice
      ConfigurableApplicationContext configContext =
          new SpringApplicationBuilder(ConfigServiceApplication.class).parent(commonContext)
              .sources(RefreshScope.class).run(args);
      logger.info(configContext.getId() + " #### isActive: " + configContext.isActive());

    //启动portalservice
    ConfigurableApplicationContext portalContext =
          new SpringApplicationBuilder(PortalApplication.class).parent(commonContext)
              .sources(RefreshScope.class).run(args);
      logger.info(portalContext.getId() + " #### isActive: " + portalContext.isActive());
  }

}
