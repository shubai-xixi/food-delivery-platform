// Orders.java
package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {
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
    private String status;
    private String items;  // JSON字符串
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime deliveredAt;
}