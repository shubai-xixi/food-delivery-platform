package com.elm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private Integer shopId;

    private String shopName;

    private Long addressId;

    private String addressSnapshot;

    private Float totalPrice;

    private Float totalDiscountPrice;

    private Float deliveryFee;

    private Float actualPay;

    private String orderType;

    private Integer refOriginalOrderId;

    private String payStatus;

    private String payMethod;

    private LocalDateTime payTime;

    private String payTransactionNo;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;
}