package com.elm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.elm.mapper")
@EnableFeignClients
public class OrderService18083Application {
    public static void main(String[] args) {
        SpringApplication.run(OrderService18083Application.class, args);
    }
}