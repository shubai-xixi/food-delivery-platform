package com.elm.enums;

public enum OrderType {
    NORMAL("普通订单"),
    REFUND("退款订单");

    private final String desc;

    OrderType(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}