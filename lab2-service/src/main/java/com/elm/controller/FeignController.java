package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.dto.RegisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/feign")
public class FeignController {

    @FeignClient(name = "user-service")
    public interface UserServiceFeign {
        //        @GetMapping("/user/info/{userId}")
        //        Result<Map<String, Object>> getUser(@PathVariable("userId") Long userId);
        @GetMapping("/user/info/{userId}")
        ResponseEntity<Result<Map<String, Object>>> getUser(@PathVariable("userId") Long userId);

        @PostMapping("/user/register")
        Result<String> register(@RequestBody RegisterDTO dto);

        @PutMapping("/address/{id}")
        Result<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressDTO dto);

        @DeleteMapping("/address/{id}")
        Result<?> deleteAddress(@PathVariable("id") Long id);
    }

    @Autowired
    private UserServiceFeign feign;

    @GetMapping("/user/{userId}")
    public Result<?> getUser(@PathVariable Long userId) {
        // 收到 ResponseEntity，可以取响应头
        ResponseEntity<Result<Map<String, Object>>> response = feign.getUser(userId);

        // 从响应头拿端口
        String port = response.getHeaders().getFirst("X-Server-Port");
        System.out.println("[Feign] 端口: " + port + " | userId: " + userId);

        // 返回原始数据
        return response.getBody();
    }

    @PostMapping("/user")
    public Result<?> addUser(@RequestBody RegisterDTO dto) {
        System.out.println("[Feign] POST body=" + dto);
        return feign.register(dto);
    }

    @PutMapping("/address/{id}")
    public Result<?> updateAddress(@PathVariable Long id, @RequestBody AddressDTO dto) {
        System.out.println("[Feign] PUT id=" + id + " body=" + dto);
        return feign.updateAddress(id, dto);
    }

    @DeleteMapping("/address/{id}")
    public Result<?> deleteAddress(@PathVariable Long id) {
        System.out.println("[Feign] DELETE id=" + id);
        return feign.deleteAddress(id);
    }
}