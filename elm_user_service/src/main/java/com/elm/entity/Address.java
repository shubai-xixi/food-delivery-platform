package com.elm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 收货地址实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("address")
public class Address {

    @TableId(type = IdType.ASSIGN_ID)

    private Long id;

    /** 用户ID */
    private Long userId;

    /** 联系人姓名 */
    private String contactName;

    /** 联系人手机号 */
    private String contactPhone;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String district;

    /** 详细地址（门牌号） */
    private String detail;

    /** 标签（如：家、公司、学校） */
    private String label;

    /** 是否默认地址 0-否 1-是 */
    private Integer isDefault;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}