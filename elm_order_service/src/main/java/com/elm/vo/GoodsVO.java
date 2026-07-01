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
public class GoodsVO {
    private Integer id;
    private String name;
    private Integer shopId;
    private String description;
    private String imageUrl;
    private String options;        // 口味偏好（温度、甜度、小料）
    private String status;
    private List<GoodsSkuVO> skus; // SKU 列表
}