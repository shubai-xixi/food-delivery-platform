package com.elm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FallbackController {
    @RequestMapping("/fallback/user")
    public Mono<Map<String, Object>> userFallback(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        // 判断降级原因
        Throwable exception = exchange.getAttribute("circuitBreaker.exception");
        String reason;
        if (exception != null) {
            String exMsg = exception.getMessage();
            if (exMsg != null && exMsg.contains("Timeout")) {
                reason = "响应超时";
                log.error("========== 熔断触发 ==========");
                log.error("[降级] 路径: {}", path);
                log.error("[原因] 响应超时 - 服务处理太慢");
                log.error("==============================");
            } else if (exMsg != null && exMsg.contains("Connection refused")) {
                reason = "连接失败（服务宕机）";
                log.error("========== 熔断触发 ==========");
                log.error("[降级] 路径: {}", path);
                log.error("[原因] 连接失败 - 用户服务已宕机");
                log.error("==============================");
            } else {
                reason = "服务不可用";
                log.error("========== 熔断触发 ==========");
                log.error("[降级] 路径: {}", path);
                log.error("[原因] 未知异常: {}", exMsg);
                log.error("==============================");
            }
        } else {
            reason = "断路器已打开";
            log.error("========== 熔断触发 ==========");
            log.error("[降级] 路径: {}", path);
            log.error("[原因] 断路器已打开，拒绝转发");
            log.error("==============================");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("msg", "用户服务暂不可用，请稍后重试（" + reason + "）");
        result.put("data", null);
        return Mono.just(result);
    }
}