package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前端的用户信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    /** 用户ID（前端可能需要，比如跳转个人主页） */
    private Long id;

    /** 姓名（我的页面展示） */
    private String username;

    /** 手机号（脱敏：138****5678） */
    private String phone;

    /** 性别（1-男 2-女 0-未知） */
    private Integer gender;

    /** 头像相对路径：/avatar/13812345678.jpg */
    private String avatar;
}