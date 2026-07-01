package com.elm.enums;

public enum OrderStatus {
    PENDING("待确认"),
    CONFIRMED("已确认"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String desc;

    OrderStatus(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}