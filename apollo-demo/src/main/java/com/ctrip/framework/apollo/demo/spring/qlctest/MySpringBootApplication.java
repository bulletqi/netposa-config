package com.ctrip.framework.apollo.demo.spring.qlctest;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Scanner;

/**
 * @author Jason Song
 */
@SpringBootApplication
@EnableApolloConfig
public class MySpringBootApplication {

  @Autowired
  private MyBean bean;

  public static void main(String[] args) {
    new SpringApplicationBuilder(MySpringBootApplication.class).run(args);
    onKeyExit();
  }

  private static void onKeyExit() {
    System.out.println("Press Enter to exit...");
    new Scanner(System.in).nextLine();
  }
}
