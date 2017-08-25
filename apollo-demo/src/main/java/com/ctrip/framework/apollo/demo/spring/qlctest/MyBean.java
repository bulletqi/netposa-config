package com.ctrip.framework.apollo.demo.spring.qlctest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyBean {

//	@Autowired
//	private RedisProperties redisProperties;

//	@Value(value = "${spring.redis.password}")
	private String name;

	@PostConstruct
	public void init(){
//		System.out.println(redisProperties.getHost());
		System.out.println(name);
	}

}
