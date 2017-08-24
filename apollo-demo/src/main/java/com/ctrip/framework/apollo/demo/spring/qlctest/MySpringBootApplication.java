package com.ctrip.framework.apollo.demo.spring.qlctest;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Jason Song
 */
@SpringBootApplication
@EnableApolloConfig(value = "netposa.base'")
public class MySpringBootApplication {

//	@Value(value = "${spring.redis.port}")
//	public String name;


	@Autowired
	private RedisProperties redisProperties;

	public static void main(String[] args) {
		new SpringApplicationBuilder(MySpringBootApplication.class).run(args);
		onKeyExit();
	}

	private static void onKeyExit() {
		System.out.println("Press Enter to exit...");
		new Scanner(System.in).nextLine();
	}


	@Bean
	public MyBean PVMFilterRegistration() {
//		System.out.println(name);
		return null;
	}
}
