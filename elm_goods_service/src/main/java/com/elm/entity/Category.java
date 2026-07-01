package com.elm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 分类名（如：中餐、西餐、奶茶） */
    private String name;

    /** 分类图标 */
    private String iconUrl;

    /** 排序 */
    private Integer sortOrder;
}