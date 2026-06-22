// UserFeignClient.java
package com.elm.feign;

import com.elm.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    // 查地址列表
    @GetMapping("/address/list")
    Result<List<Map<String, Object>>> getAddressList(@RequestHeader("X-User-Id") Long userId);

    // 查用户信息（新增）
    @GetMapping("/user/info/{userId}")
    Result<Map<String, Object>> getUserInfo(@PathVariable("userId") Long userId);
}