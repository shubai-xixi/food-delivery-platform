package com.elm.exception;

import com.elm.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        String className = e.getClass().getSimpleName();

        if (className.contains("BulkheadFullException")) {
            log.warn("[隔离降级] 信号量已满");
            return Result.fail(503, "地址服务繁忙，请稍后重试（隔离限制）");
        }

        if (className.contains("RequestNotPermitted")) {
            log.warn("[限流降级] 请求被限流");
            return Result.fail(429, "操作太频繁，请稍后重试（限流控制）");
        }

        if (className.contains("CallNotPermittedException")) {
            log.warn("[断路器降级] 断路器打开");
            return Result.fail(500, "服务暂不可用，请稍后重试");
        }

        log.error("未知异常: {}", e.getMessage());
        return Result.fail(500, "系统繁忙，请稍后重试");
    }
}