package com.elm.aspect;

import com.elm.annotation.Log;
import com.elm.entity.OperateLog;
import com.elm.mapper.OperateLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAspect {

    private final OperateLogMapper operateLogMapper;

    @Around("@annotation(com.elm.annotation.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        Object result = null;
        boolean success = true;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            success = false;
            throw e;
        } finally {
            long costTime = System.currentTimeMillis() - start;
            try {
                OperateLog operateLog = OperateLog.builder()
                        .operateUser(getOperator())
                        .operateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .className(joinPoint.getTarget().getClass().getName())
                        .methodName(method.getName())
                        .methodParams(Arrays.toString(joinPoint.getArgs()))
                        .returnValue(success ? (result != null ? result.toString() : null) : "异常")
                        .costTime(costTime)
                        .build();
                operateLogMapper.insert(operateLog);
            } catch (Exception e) {
                log.warn("操作日志入库失败：{}", e.getMessage());
            }
        }
    }

    private String getOperator() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("Authorization");
                if (token != null && token.length() > 20) {
                    return "user:" + token.substring(7, 20) + "...";
                }
            }
        } catch (Exception ignored) {}
        return "anonymous";
    }
}