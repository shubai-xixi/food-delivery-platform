package com.elm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  // 这一行告诉Spring：我是Eureka注册中心
public class Server18001Application {
    public static void main(String[] args) {
        SpringApplication.run(Server18001Application.class, args);
    }
}