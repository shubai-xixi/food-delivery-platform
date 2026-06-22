package com.elm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.elm.mapper")
public class UserService18081Application {
    public static void main(String[] args) {
        SpringApplication.run(UserService18081Application.class, args);
    }
}