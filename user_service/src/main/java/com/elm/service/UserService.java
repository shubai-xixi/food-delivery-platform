// UserService.java
package com.elm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.dto.LoginDTO;
import com.elm.dto.RegisterDTO;
import com.elm.entity.User;
import com.elm.mapper.UserMapper;
import com.elm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String register(RegisterDTO dto) {
        User exist = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone())
        );
        if (exist != null) {
            throw new RuntimeException("手机号已注册");
        }

        User user = new User();
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        user.setRole("USER");
        userMapper.insert(user);

        return JwtUtil.generate(user.getId(), user.getPhone());
    }

    public String login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, dto.getPhone())
        );
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return JwtUtil.generate(user.getId(), user.getPhone());
    }

    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
}