package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.dto.RegisterDTO;
import com.elm.fallback.UserServiceFallbackFactory;
import com.elm.feign.UserServiceFeign;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/feign")
public class FeignController {

//    @FeignClient(name = "user-service")
//    public interface UserServiceFeign {
//        //        @GetMapping("/user/info/{userId}")
//        //        Result<Map<String, Object>> getUser(@PathVariable("userId") Long userId);
//        @GetMapping("/user/info/{userId}")
//        ResponseEntity<Result<Map<String, Object>>> getUser(@PathVariable("userId") Long userId);
//
//        @PostMapping("/user/register")
//        Result<String> register(@RequestBody RegisterDTO dto);
//
//        @PutMapping("/address/{id}")
//        Result<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressDTO dto);
//
//        @DeleteMapping("/address/{id}")
//        Result<?> deleteAddress(@PathVariable("id") Long id);
//    }

    @org.springframework.beans.factory.annotation.Value("${resilience4j.bulkhead.instances.bulkheadService.max-concurrent-calls:NOT_FOUND}")
    private String maxCalls;

    @org.springframework.beans.factory.annotation.Value("${resilience4j.bulkhead.instances.bulkheadService.max-wait-duration:NOT_FOUND}")
    private String maxWait;

    @jakarta.annotation.PostConstruct
    public void init() {
        log.error("========== max-concurrent-calls = {} ==========", maxCalls);
        log.error("========== max-wait-duration = {} ==========", maxWait);
    }

    @Autowired
    private UserServiceFeign feign;

    @GetMapping("/user/{userId}")
    @CircuitBreaker(name = "circuitBreakerA", fallbackMethod = "getUserFallbackA")
    public Result<?> getUser(@PathVariable Long userId) {
        // 收到 ResponseEntity，可以取响应头
        ResponseEntity<Result<Map<String, Object>>> response = feign.getUser(userId);

        // 从响应头拿端口
        String port = response.getHeaders().getFirst("X-Server-Port");
        System.out.println("[Feign] 端口: " + port + " | userId: " + userId);

        // 返回原始数据
        return response.getBody();
    }

    public Result<?> getUserFallbackA(Long userId, Exception e) {
        log.warn("[降级A] userId={}, 异常: {}", userId, e.getMessage());
        return Result.fail(500, "用户服务不可用，请稍后重试（断路器A已打开）");
    }

    @PostMapping("/user")
    @CircuitBreaker(name = "circuitBreakerB", fallbackMethod = "addUserFallbackB")
    public Result<?> addUser(@RequestBody RegisterDTO dto) {

        System.out.println("[Feign] POST body=" + dto);

        try {
            Thread.sleep(3000);  // 模拟慢调用，3 秒 > 2 秒阈值
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return feign.register(dto);
    }

    public Result<?> addUserFallbackB(RegisterDTO dto, Exception e) {
        log.warn("[降级B] 注册降级, 异常: {}", e.getMessage());
        return Result.fail(500, "注册服务不可用，请稍后重试（断路器B已打开）");
    }


    @PutMapping("/address/{id}")
//    @Bulkhead(name = "bulkheadService", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "updateAddressFallback")
    @Bulkhead(name = "bulkheadService", fallbackMethod = "updateAddressFallback")
    public Result<?> updateAddress(@PathVariable Long id, @RequestBody AddressDTO dto) {
        log.info("[Feign] PUT id={} body={}", id, dto);

        Result<?> result = feign.updateAddress(id, dto);  // ← 先调 Feign，Bulkhead 开始计数

        try {
            Thread.sleep(5000);  // ← 返回结果后睡 5 秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return result;
    }

    public Result<?> updateAddressFallback(Long id, AddressDTO dto, Exception e) {
        log.warn("[隔离降级] id={}, 异常: {}", id, e.getMessage());
        return Result.fail(503, "地址服务繁忙，请稍后重试（隔离限制）");
    }


    @DeleteMapping("/address/{id}")
    @RateLimiter(name = "rateLimiterService", fallbackMethod = "deleteAddressFallback")
    public Result<?> deleteAddress(@PathVariable Long id) {
        System.out.println("[Feign] DELETE id=" + id);
        return feign.deleteAddress(id);
    }

    public Result<?> deleteAddressFallback(Long id, Exception e) {
        log.warn("[限流降级] id={}, 异常: {}", e.getMessage());
        return Result.fail(429, "操作太频繁，请稍后重试（限流控制）");
    }
}