package com.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.common.Result;
import com.elm.common.ResultCode;
import com.elm.dto.*;
import com.elm.entity.User;
import com.elm.mapper.UserMapper;
import com.elm.service.UserService;
import com.elm.util.JwtUtil;
import com.elm.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${avatar.upload-dir:/avatar/}")
    private String avatarDir;

    // ==================== 注册 ====================

    @Override
    @Transactional
    public Result<?> register(RegisterDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, dto.getPhone());
        if (userMapper.selectOne(wrapper) != null) {
            return Result.fail(ResultCode.PHONE_EXIST);
        }

        User user = User.builder()
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .gender(dto.getGender() != null ? dto.getGender() : 0)
                .role("USER")
                .avatar(null)
                .build();

        userMapper.insert(user);
        log.info("注册成功，手机号：{}", dto.getPhone());

        return Result.ok();  // ← 只返回成功，不带数据
    }

    // ==================== 登录 ====================

    @Override
    public Result<TokenVO> login(LoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, dto.getPhone());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            return Result.fail(ResultCode.PHONE_NOT_EXIST);
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.fail(ResultCode.PASSWORD_ERROR);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getPhone(), user.getRole());
        log.info("登录成功，手机号：{}", dto.getPhone());
        return Result.ok(new TokenVO(token));
    }

    // ==================== 修改个人信息 ====================

    @Override
    @Transactional
    public Result<UserVO> updateProfile(Long userId, UpdateProfileDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail(ResultCode.NOT_FOUND);
        }

        if (dto.getUsername() != null) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }

        userMapper.updateById(user);
        log.info("个人信息更新成功，用户ID：{}", userId);
        return Result.ok(toVO(user));
    }

    // ==================== 上传头像 ====================

    @Override
    @Transactional
    public Result<UserVO> uploadAvatar(Long userId, MultipartFile file) {
        // 1. 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.fail(400, "只能上传图片文件");
        }

        // 2. 校验文件大小（加一个，防止传超大文件）
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.fail(400, "图片大小不能超过2MB");
        }

        // 3. 生成文件名
        String originalName = file.getOriginalFilename();
        String extension = ".jpg";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = userId + extension;

        // 4. 用系统临时目录或项目根目录的绝对路径
        //    避免相对路径在不同环境出问题
        String basePath = System.getProperty("user.dir");  // 项目根目录
        Path uploadPath = Paths.get(basePath, "uploads", "avatar");
        log.info("头像上传目录：{}", uploadPath.toAbsolutePath());

        // 5. 创建目录
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("目录创建成功：{}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("创建上传目录失败", e);
            return Result.fail(500, "服务器错误：无法创建上传目录");
        }

        // 6. 保存文件
        Path targetFile = uploadPath.resolve(fileName);
        try {
            file.transferTo(targetFile.toFile());
            log.info("头像文件保存成功：{}", targetFile.toAbsolutePath());
        } catch (IOException e) {
            log.error("保存文件失败，目标路径：{}", targetFile.toAbsolutePath(), e);
            return Result.fail(500, "上传失败，请重试");
        }

        // 7. 更新数据库
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        String avatarPath = avatarDir + fileName;
        user.setAvatar(avatarPath);
        userMapper.updateById(user);

        log.info("头像更新成功，用户ID：{}，路径：{}", userId, avatarPath);
        return Result.ok(toVO(user));
    }

    // ==================== 获取个人信息 ====================

    @Override
    public Result<UserVO> getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        return Result.ok(toVO(user));
    }

    // ==================== 工具方法 ====================

    private UserVO toVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(desensitize(user.getPhone()))
                .gender(user.getGender())
                .avatar(user.getAvatar())
                .build();
    }

    private String desensitize(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}