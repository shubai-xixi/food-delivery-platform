package com.elm.controller;

import com.elm.common.Result;
import com.elm.common.UserContext;
import com.elm.dto.*;
import com.elm.vo.*;
import com.elm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Tag(name = "用户模块", description = "注册、登录、个人信息管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

//    @Operation(summary = "用户注册")
//    @PostMapping("/register")
//    @RateLimiter(name = "register", fallbackMethod = "registerFallback")
//    public Result<?> register(@RequestBody RegisterDTO dto) {
//        return userService.register(dto);
//    }
//
//    @Operation(summary = "用户登录")
//    @PostMapping("/login")
//    @RateLimiter(name = "login", fallbackMethod = "loginFallback")
//    public Result<TokenVO> login(@RequestBody LoginDTO dto) {
//        return userService.login(dto);
//    }

    @PostMapping("/register")
    @RateLimiter(name = "register", fallbackMethod = "registerFallback")
    public Result<?> register(@RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }

    public Result<?> registerFallback(RegisterDTO dto, Throwable t) {
        log.warn("注册限流 phone={}", dto.getPhone());
        return Result.fail(429, "注册太频繁，请稍后重试");
    }

    @PostMapping("/login")
    @RateLimiter(name = "login", fallbackMethod = "loginFallback")
    public Result<TokenVO> login(@RequestBody LoginDTO dto) {
        return userService.login(dto);
    }

    public Result<TokenVO> loginFallback(LoginDTO dto, Throwable t) {
        log.warn("登录限流 phone={}", dto.getPhone());
        return Result.fail(429, "登录太频繁，请稍后重试");
    }

    @Operation(summary = "获取个人信息")
    @GetMapping("/me")
    public Result<UserVO> getProfile() {
        Long userId = UserContext.getUserId();
        return userService.getProfile(userId);
    }

    @Operation(summary = "修改个人信息（姓名、性别）")
    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestBody UpdateProfileDTO dto) {
        Long userId = UserContext.getUserId();
        return userService.updateProfile(userId, dto);
    }

    @Operation(summary = "上传头像")
    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    public Result<UserVO> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = UserContext.getUserId();
        return userService.uploadAvatar(userId, file);
    }
}