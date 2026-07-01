package com.elm.annotation;


import java.lang.annotation.*;

/**
 * 操作日志注解
 * value：操作描述，如 "新增地址"、"修改个人信息"
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";
}