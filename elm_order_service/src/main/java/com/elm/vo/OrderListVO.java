package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListVO {
    private Integer id;
    private String shopName;
    private String addressSnapshot;
    private Float totalPrice;
    private Float deliveryFee;
    private Float actualPay;
    private String payStatus;
    private String status;
    private String createdAt;
}