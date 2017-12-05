package com.ctrip.framework.apollo.configservice;

import com.ctrip.framework.apollo.configservice.wonderproperties.WonderPropertiesCompent;
import com.ctrip.framework.apollo.core.NetposaConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executors;

//系统初始化
@Component
public class SystemPropertiesInitializer  implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger logger = LoggerFactory.getLogger(SystemPropertiesInitializer.class);

	@Autowired
	private WonderPropertiesCompent wonderPropertiesCompent;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Executors.newSingleThreadExecutor().execute(new Runnable(){
			@Override
			public void run() {
				NetposaConstant.globalLock.lock();
				try{
					NetposaConstant.condition.await();
					wonderPropertiesCompent.initSystem();
				} catch (Exception e) {
					logger.error("初始化系统配置失败",e);
				}finally {
					NetposaConstant.globalLock.unlock();
				}
			}
		});
	}

}
