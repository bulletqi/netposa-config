package com.ctrip.framework.apollo.spring.util;

import com.ctrip.framework.apollo.core.utils.NetposaConstant;
import org.springframework.core.env.Environment;

public class PropertiesContext {

    public static void setNetposaProperties(Environment environment) {
        NetposaConstant.appId = environment.getProperty("spring.application.name");
        NetposaConstant.devMeta = environment.getProperty("spring.netposa.center-conf.url");
        NetposaConstant.isEnable = environment.getProperty("spring.netposa.center-conf.enable",Boolean.class);
    }
}
