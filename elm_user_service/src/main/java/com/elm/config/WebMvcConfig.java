package com.elm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置：静态资源映射
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String basePath = System.getProperty("user.dir");
        String avatarLocation = "file:" + basePath + "/uploads/avatar/";
        log.info("静态资源映射：/avatar/** → {}", avatarLocation);

        registry.addResourceHandler("/avatar/**")
                .addResourceLocations(avatarLocation);
    }
}