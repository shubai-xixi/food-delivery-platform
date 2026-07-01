package com.elm.controller;

import com.elm.common.Result;
import com.elm.common.UserContext;
import com.elm.dto.OrderDTO;
import com.elm.service.OrderService;
import com.elm.vo.OrderListVO;
import com.elm.vo.OrderVO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单模块", description = "生成订单、支付、查看")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "生成订单")
    @PostMapping
    @RateLimiter(name = "createOrder", fallbackMethod = "createOrderFallback")
    @Bulkhead(name = "createOrder", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "createOrder", fallbackMethod = "createOrderFallback")
    public Result<OrderVO> create(@RequestBody OrderDTO dto) {
        Long userId = UserContext.getUserId();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return orderService.create(userId, dto);
    }

    @Operation(summary = "支付订单")
    @PutMapping("/{id}/pay")
    @RateLimiter(name = "payOrder", fallbackMethod = "payFallback")
    @Bulkhead(name = "payOrder", fallbackMethod = "payFallback")
    @CircuitBreaker(name = "payOrder", fallbackMethod = "payFallback")
    public Result<?> pay(@PathVariable Integer id,
                         @RequestParam(defaultValue = "SIMULATE") String payMethod) {
        Long userId = UserContext.getUserId();
        return orderService.pay(userId, id, payMethod);
    }


    public Result<OrderVO> createOrderFallback(OrderDTO dto, Throwable t) {
        String type = t.getClass().getSimpleName();
        if (type.contains("RequestNotPermitted")) {
            log.warn("【限流】下单被限流");
            return Result.fail(429, "下单太频繁，请稍后重试");
        } else if (type.contains("BulkheadFull")) {
            log.warn("【隔离】下单隔离触发");
            return Result.fail(503, "服务繁忙，请稍后重试");
        } else {
            log.warn("【熔断】下单熔断触发");
            return Result.fail(503, "服务暂不可用，请稍后重试");
        }
    }

    public Result<?> payFallback(Integer id, String payMethod, Throwable t) {
        String type = t.getClass().getSimpleName();
        if (type.contains("RequestNotPermitted")) {
            log.warn("【限流】支付被限流 orderId={}", id);
            return Result.fail(429, "支付太频繁，请稍后重试");
        } else if (type.contains("BulkheadFull")) {
            log.warn("【隔离】支付隔离触发 orderId={}", id);
            return Result.fail(503, "服务繁忙，请稍后重试");
        } else {
            log.warn("【熔断】支付熔断触发 orderId={}", id);
            return Result.fail(503, "服务暂不可用，请稍后重试");
        }
    }


    @GetMapping
    public Result<List<OrderListVO>> list() {
        Long userId = UserContext.getUserId();
        return orderService.list(userId);
    }

    @Operation(summary = "查看订单详情")
    @GetMapping("/{id}")
    public Result<OrderVO> detail(@PathVariable Integer id) {
        Long userId = UserContext.getUserId();
        return orderService.detail(userId, id);
    }
}