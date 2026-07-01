package com.elm.enums;

public enum PayMethod {
    WECHAT("微信支付"),
    ALIPAY("支付宝"),
    SIMULATE("模拟支付");

    private final String desc;

    PayMethod(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}