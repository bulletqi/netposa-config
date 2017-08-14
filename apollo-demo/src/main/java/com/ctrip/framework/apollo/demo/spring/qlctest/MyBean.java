package com.ctrip.framework.apollo.demo.spring.qlctest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MyBean {

//	@Value(value="${spring.redis.host}")
	public   String name;

	@PostConstruct
	public void init(){
		System.out.println(name);
	}

}
