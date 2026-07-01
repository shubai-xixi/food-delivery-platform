package com.elm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("cart")
public class Cart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private Integer shopId;
    private Integer goodsId;         // 加回来
    private Integer skuId;
    private String preferences;
    private Integer quantity;
    private Integer isChecked;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}