package com.elm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.elm")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.elm.feign")
@MapperScan({"com.elm.mapper", "com.elm.mapper"})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("========================================");
        System.out.println("  订单服务启动成功！");
        System.out.println("  端口: 16020");
        System.out.println("  Swagger: http://localhost:16020/swagger-ui/index.html");
        System.out.println("========================================");
    }
}