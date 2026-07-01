package com.elm.config;

import com.elm.interceptor.TokenInterceptor;
import com.elm.util.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonWebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

    public CommonWebMvcConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(jwtUtil))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/register",
                        "/user/login",
                        "/categories",
                        "/merchants",
                        "/merchants/**",
                        "/avatar/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }
}