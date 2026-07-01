package com.elm.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 功能：生成Token、解析Token、校验Token
 */
@Slf4j
@Component
public class JwtUtil {

    /** 密钥（从配置文件读取，至少256位） */
    @Value("${jwt.secret:default-secret-key-must-be-at-least-256-bits-long-for-hs256}")
    private String secret;

    /** Token过期时间（毫秒），默认7天 */
    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     *
     * @param userId 用户ID
     * @param phone  手机号
     * @param role   角色
     * @return JWT Token 字符串
     */
    public String generateToken(Long userId, String phone, String role) {
        // 当前时间
        long now = System.currentTimeMillis();

        // 自定义字段
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("phone", phone);
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)                          // 自定义数据
                .subject(String.valueOf(userId))        // 主题：用户ID
                .issuedAt(new Date(now))                // 签发时间
                .expiration(new Date(now + expiration))  // 过期时间
                .signWith(getSigningKey())               // 签名
                .compact();
    }

    /**
     * 解析 Token，获取 Claims
     *
     * @param token JWT Token
     * @return Claims
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期：{}", e.getMessage());
            throw new RuntimeException("Token已过期，请重新登录");
        } catch (JwtException e) {
            log.warn("Token无效：{}", e.getMessage());
            throw new RuntimeException("Token无效");
        }
    }

    /**
     * 从 Token 中获取用户ID
     */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Token 中获取手机号
     */
    public String getPhone(String token) {
        Claims claims = parseToken(token);
        return claims.get("phone", String.class);
    }

    /**
     * 从 Token 中获取角色
     */
    public String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    /**
     * 判断 Token 是否过期
     */
    public boolean isExpired(String token) {
        try {
            parseToken(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 校验 Token 是否有效
     */
    public boolean validate(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}