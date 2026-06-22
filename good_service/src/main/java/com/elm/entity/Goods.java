// Goods.java
package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer shopId;
    private Integer categoryId;
    private Float price;
    private Float estimatedPrice;
    private Integer positiveRate;
    private Integer monthlySales;
    private String description;
    private String ingredients;
    private String portion;
    private String imageUrl;
    private String sizes;       // JSON字符串，MyBatis-Plus映射为String
    private String temperatures;
    private String sweetness;
    private String status;
    private LocalDateTime createdAt;
}