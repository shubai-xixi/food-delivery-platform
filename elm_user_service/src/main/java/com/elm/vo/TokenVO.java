package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    /** JWT Token */
    private String token;
}