package com.ctrip.framework.apollo.configservice;

import com.ctrip.framework.apollo.ConfigServiceApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Entry point for traditional web app
 *
 * @author Jason Song(song_s@ctrip.com)
 */
public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ConfigServiceApplication.class);
  }

}
