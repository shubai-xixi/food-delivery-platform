// Merchant.java
package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("merchant")
public class Merchant {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String shopName;
    private Integer categoryId;
    private Float rating;
    private Float minPrice;
    private Float deliveryFee;
    private Integer estimatedMinTime;
    private Integer estimatedMaxTime;
    private Integer monthlySales;
    private String imageUrl;
    private String announcement;
    private String address;
    private String status;
    private LocalDateTime createdAt;
}