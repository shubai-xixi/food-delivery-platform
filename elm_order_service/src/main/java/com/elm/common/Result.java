// Result.java
package com.elm.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一返回结果，支持熔断降级
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    /** 状态码 */
    private int code;

    /** 消息 */
    private String msg;

    /** 数据 */
    private T data;

    /** 时间戳 */
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 成功 ====================

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    // ==================== 失败 ====================

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> fail(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    // ==================== 降级 ====================

    /**
     * 服务降级专用
     */
    public static <T> Result<T> degrade(String msg) {
        return new Result<>(503, msg, null);
    }

    /**
     * 服务降级带数据（返回兜底数据）
     */
    public static <T> Result<T> degrade(String msg, T fallbackData) {
        return new Result<>(503, msg, fallbackData);
    }

    // ==================== 限流 ====================

    /**
     * 限流专用
     */
    public static <T> Result<T> limit(String msg) {
        return new Result<>(429, msg, null);
    }

    // ==================== Getter/Setter ====================

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}