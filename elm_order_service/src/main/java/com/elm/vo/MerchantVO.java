package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantVO {
    private Integer id;
    private String shopName;
    private Integer categoryId;
    private Float rating;
    private Float minPrice;
    private Float deliveryFee;
    private String estimatedTime;  // "25-35分钟"
    private Integer monthlySales;
    private String imageUrl;
    private String address;
    private String status;
}