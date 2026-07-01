package com.elm.dto;

import lombok.Data;

@Data
public class CartDTO {
    private Integer shopId;
    private Integer goodsId;       // 新增
    private Integer skuId;
    private String preferences;
    private Integer quantity;
}