package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private Integer id;
    private String shopName;
    private String addressSnapshot;  // 地址快照
    private Float totalPrice;
    private Float deliveryFee;
    private Float actualPay;
    private String payStatus;
    private String status;
    private List<OrderItemVO> items;
}