package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVO {
    private String goodsName;
    private String specName;
    private String preferences;
    private Float basePrice;
    private Float addonPrice;
    private Float finalPrice;
    private Integer quantity;
}