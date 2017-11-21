package com.ctrip.framework.apollo.spring.util;

import com.ctrip.framework.apollo.core.NetposaConstant;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class PropertiesContext {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesContext.class);
    
    public static final String VIAS_CONFIG_CENTER_ADDRESS = "VIAS_CONFIG_CENTER_ADDRESS";
    
    public static void setNetposaProperties(Environment environment) {
        NetposaConstant.appId = environment.getProperty("spring.application.name");
        String url = environment.getProperty(VIAS_CONFIG_CENTER_ADDRESS);
        if (StringUtils.isEmpty(url)) {
            url = environment.getProperty("spring.netposa.center-conf.url");
        }
        NetposaConstant.devMeta = url;
        NetposaConstant.isEnable = environment.getProperty("spring.netposa.center-conf.enable", Boolean.class);
        
        LOGGER.info("---- Config Center with [ appId={} , server={} , enable={}] ----",
                NetposaConstant.appId,
                NetposaConstant.devMeta,
                NetposaConstant.isEnable
        );
    }
}
