// UserController.java
package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.LoginDTO;
import com.elm.dto.RegisterDTO;
import com.elm.entity.User;
import com.elm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 在 UserController.java 里加这个接口
    @GetMapping("/info/{userId}")
    public Result<User> getUserInfo(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        // 返回前把密码清掉，即使是密文也不能暴露给其他服务
        user.setPassword(null);
        return Result.ok(user);
    }
}