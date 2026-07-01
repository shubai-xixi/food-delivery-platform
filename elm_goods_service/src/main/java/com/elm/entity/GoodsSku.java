package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("goods_sku")
public class GoodsSku {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer goodsId;
    private String specName;
    private Float price;
    private Integer stock;
    private Integer isDefault;
}
