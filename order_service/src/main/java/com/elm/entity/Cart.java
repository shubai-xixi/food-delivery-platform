// ========== order-service Entity ==========

// Cart.java
package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private Integer shopId;
    private Integer goodsId;
    private String goodsName;
    private String goodsImage;
    private String selectedSize;
    private String selectedTemperature;
    private String selectedSweetness;
    private Float price;
    private Integer quantity;
    private LocalDateTime createdAt;
}