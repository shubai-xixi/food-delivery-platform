package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartVO {
    private Integer id;
    private Integer goodsId;
    private Integer skuId;
    private String preferences;  // {"温度":"去冰","甜度":"半糖","小料":["珍珠"]}
    private Integer quantity;
}