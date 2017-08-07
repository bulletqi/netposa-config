package com.ctrip.framework.apollo.demo.api;

import com.ctrip.framework.apollo.*;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.ctrip.framework.foundation.Foundation;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class MyConfigDemo {

  public static void main(String[] args) throws IOException {
    Config appConfig = ConfigService.getConfig("ymldemo.yml");
    System.out.println(appConfig.getProperty("content",""));
  }
}
