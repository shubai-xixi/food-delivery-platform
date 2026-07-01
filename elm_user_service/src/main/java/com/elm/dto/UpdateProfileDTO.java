package com.elm.dto;

import lombok.Data;

/**
 * 修改个人信息请求
 */
@Data
public class UpdateProfileDTO {
    /** 姓名 */
    private String username;
    /** 性别：1-男 2-女 */
    private Integer gender;
    /** 头像URL */
//    private String avatar;
}