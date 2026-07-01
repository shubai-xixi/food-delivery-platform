package com.elm.dto;

import lombok.Data;

/**
 * 注册请求
 */
@Data
public class RegisterDTO {
    /** 手机号 */
    private String phone;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String username;
    /** 性别：1-男 2-女 */
    private Integer gender;
}