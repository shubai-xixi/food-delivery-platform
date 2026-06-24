package com.elm.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 放行登录和注册
        if (path.contains("/login")
                || path.contains("/register")
                || path.contains("/swagger")
                || path.contains("/api-docs")
                || path.contains("/webjars")) {
            log.info("[Auth] 放行路径: {}", path);
            return chain.filter(exchange);
        }

        // 检查 token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("[Auth] 未授权访问: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        log.info("[Auth] 认证通过: {}", path);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}