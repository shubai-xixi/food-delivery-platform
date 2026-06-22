package com.elm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.elm.mapper")
public class GoodsService18082Application {
    public static void main(String[] args) {
        SpringApplication.run(GoodsService18082Application.class, args);
    }
}