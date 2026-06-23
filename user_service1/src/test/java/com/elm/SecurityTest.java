package com.elm;

import com.elm.util.JwtUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SecurityTest {

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testBcrypt() {
        String rawPassword = "123456";
        String encoded = passwordEncoder.encode(rawPassword);
        System.out.println("加密后：" + encoded);

        boolean match = passwordEncoder.matches(rawPassword, encoded);
        System.out.println("验证结果：" + match);
    }

    @Test
    void testJwt() {
        String token = JwtUtil.generate(1L, "13800138000");
        System.out.println("Token：" + token);

        Long userId = JwtUtil.getUserId(token);
        System.out.println("解析出的userId：" + userId);
    }
}