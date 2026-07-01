package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSkuVO {
    private Integer id;
    private String specName;   // "大杯"
    private Float price;       // 25.00
    private Integer stock;     // 100
    private Integer isDefault; // 1=默认选中
}