package com.elm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务启动类
 */
//@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan("com.elm.mapper")
@SpringBootApplication(scanBasePackages = "com.elm")
@MapperScan({"com.elm.mapper", "com.elm.mapper"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("========================================");
        System.out.println("  用户服务启动成功！");
        System.out.println("  端口: 16001");
        System.out.println("  Swagger: http://localhost:16001/swagger-ui/index.html");
        System.out.println("========================================");
    }
}
