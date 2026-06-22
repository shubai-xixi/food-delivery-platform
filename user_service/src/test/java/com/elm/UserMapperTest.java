package com.elm;

import com.elm.entity.User;
import com.elm.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserService18081Application.class)
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("张三");
        user.setPhone("13800138000");
        user.setPassword("123456");
        user.setRole("USER");
        userMapper.insert(user);
        System.out.println("插入成功，ID：" + user.getId());
    }

    @Test
    void testSelect() {
        User user = userMapper.selectById(1L);
        System.out.println("查询结果：" + user);
    }
}