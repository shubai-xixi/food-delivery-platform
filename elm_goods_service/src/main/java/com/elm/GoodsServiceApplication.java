package com.elm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.elm.mapper")
public class GoodsServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GoodsServiceApplication.class, args);
        String port = context.getEnvironment().getProperty("server.port");
        System.out.println("========================================");
        System.out.println("  商品服务启动成功！");
        System.out.println("  端口: " + port);
        System.out.println("  Swagger: http://localhost:" + port + "/swagger-ui/index.html");
        System.out.println("========================================");
    }
}