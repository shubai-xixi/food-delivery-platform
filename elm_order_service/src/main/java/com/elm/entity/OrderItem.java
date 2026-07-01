package com.elm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_item")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    private Integer goodsId;

    private String goodsName;

    private Integer skuId;

    private String specName;

    private Float basePrice;

    private String preferences;   // JSON

    private Float addonPrice;

    private Float finalPrice;

    private Integer quantity;
}