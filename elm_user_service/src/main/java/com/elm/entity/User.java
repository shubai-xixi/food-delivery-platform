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
@TableName("users")
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户名 */
    private String username;

    /** 性别：0-未知 1-男 2-女 */
    private Integer gender;

    /** 手机号，登录用 */
    private String phone;

    /** BCrypt加密密文 */
    private String password;

    /** 角色：USER/ADMIN */
    private String role;

    /** 头像URL */
    private String avatar;

    /** 注册时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}