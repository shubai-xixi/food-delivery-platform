// UserController.java
package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.LoginDTO;
import com.elm.dto.RegisterDTO;
import com.elm.entity.User;
import com.elm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterDTO dto) {
        String token = userService.register(dto);
        return Result.ok(token);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return Result.ok(token);
    }

    @Value("${server.port}")
    private String port;

    @GetMapping("/info/{userId}")
    public ResponseEntity<Result<User>> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        user.setPassword(null);

        return ResponseEntity
                .ok()
                .header("X-Server-Port", port)   // ← 响应头加端口
                .body(Result.ok(user));
    }
}