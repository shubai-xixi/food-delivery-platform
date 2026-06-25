package com.elm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/test")
public class TestController {

    @Value("${test.message}")  // 启动时注入一次
    private String message;    // 之后永远不会变

    @GetMapping("/msg")
    public String getMsg() {
        return message;  // 返回的永远是启动时的值
    }
}