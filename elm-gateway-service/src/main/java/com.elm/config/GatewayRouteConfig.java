package com.elm.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/user/**", "/address/**")
                        .uri("lb://elm-user-service"))
                .route("goods-service", r -> r
                        .path("/categories/**", "/merchants/**", "/goods/**")
                        .uri("lb://elm-goods-service"))
                .route("order-service", r -> r
                        .path("/orders/**", "/cart/**")
                        .uri("lb://elm-order-service"))
                .build();
    }
}