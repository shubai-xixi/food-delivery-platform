package com.elm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
public class Comment {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 评价用户ID */
    private Long userId;

    /** 商家ID */
    private Integer shopId;

    /** 商品ID（可为空） */
    private Integer goodsId;

    /** 关联订单ID */
    private Integer orderId;

    /** 父评论ID（NULL是一级评论，有值是回复） */
    private Integer parentId;

    /** 评分（1-5星） */
    private Integer rating;

    /** 评价内容 */
    private String content;

    /** 图片列表（JSON数组，最多9张） */
    private String picUrls;

    /** 点赞数 */
    private Integer likes;

    /** 是否匿名（0否，1是） */
    private Integer isAnonymous;

    /** 状态：SHOW / HIDE / AUDIT */
    private String status;

    /** 评价日期 */
    private LocalDate commentDate;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}