package com.elm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("address")
public class Address {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String contactName;
    private String contactPhone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String label;
    private Integer isDefault;
    private LocalDateTime createdAt;
}