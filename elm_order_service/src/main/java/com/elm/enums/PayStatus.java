package com.elm.enums;

public enum PayStatus {
    UNPAID("待支付"),
    PAID("已支付"),
    FAIL("支付失败"),
    REFUNDING("退款中"),
    REFUNDED("已退款");

    private final String desc;

    PayStatus(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}