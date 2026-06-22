// Comment.java
package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long userId;
    private Integer shopId;
    private Integer goodsId;
    private Integer orderId;
    private Integer rating;
    private String content;
    private String picUrl;
    private Integer likes;
    private LocalDate commentDate;
}