package com.dianxin.oauth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dianxin")
public class OauthApplication {
     public static void main(String[] args) {
         SpringApplication.run(OauthApplication.class, args);
         System.out.println("----------- 登录认证服务启动成功 ----------------");
     }

}
