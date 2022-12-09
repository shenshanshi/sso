package com.dianxin.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dianxin")
public class UserApplication {
     public static void main(String[] args) {
         SpringApplication.run(UserApplication.class, args);
         System.out.println("----------- 用户服务启动成功 ----------------");
     }

}
