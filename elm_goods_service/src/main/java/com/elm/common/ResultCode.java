package com.elm.common;

/**
 * 统一错误码
 */
public enum ResultCode {

    // 4xx 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或Token过期"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    PHONE_EXIST(4001, "手机号已注册"),
    PHONE_NOT_EXIST(4002, "手机号未注册"),
    PASSWORD_ERROR(4003, "密码错误"),

    // 5xx 服务端错误
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),
    DEGRADE(5031, "服务降级中"),
    LIMIT(429, "请求过于频繁，请稍后再试"),

    // 通用
    SUCCESS(200, "success");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() { return code; }
    public String getMsg() { return msg; }
}