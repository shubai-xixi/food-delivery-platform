package com.elm.service;

import com.elm.common.Result;
import com.elm.dto.*;
import com.elm.vo.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /** 注册 */
    Result<?> register(RegisterDTO dto);

    /** 登录 */
    Result<TokenVO> login(LoginDTO dto);

    /** 修改个人信息（姓名、性别） */
    Result<UserVO> updateProfile(Long userId, UpdateProfileDTO dto);

    /** 上传头像 */
    Result<UserVO> uploadAvatar(Long userId, MultipartFile file);

    /** 获取个人信息 */
    Result<UserVO> getProfile(Long userId);
}