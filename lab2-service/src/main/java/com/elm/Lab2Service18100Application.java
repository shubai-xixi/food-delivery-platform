package com.elm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient      // ← 开启服务注册发现
@EnableFeignClients         // ← 开启 Feign
public class Lab2Service18100Application {
    public static void main(String[] args) {
        SpringApplication.run(Lab2Service18100Application.class, args);
    }
}