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
@TableName("merchant")
public class Merchant {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 店铺名称 */
    private String shopName;

    /** 所属分类ID */
    private Integer categoryId;

    /** 评分 */
    private Float rating;

    /** 起送价 */
    private Float minPrice;

    /** 配送费 */
    private Float deliveryFee;

    /** 预计最短送达时间（分钟） */
    private Integer estimatedMinTime;

    /** 预计最长送达时间（分钟） */
    private Integer estimatedMaxTime;

    /** 月销量 */
    private Integer monthlySales;

    /** 店铺图片 */
    private String imageUrl;

    /** 商家地址 */
    private String address;

    /** 营业状态：OPEN/CLOSED */
    private String status;

    /** 入驻时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}