package com.elm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elm.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}