package com.elm.feign;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.dto.RegisterDTO;
import com.elm.fallback.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "user-service", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserServiceFeign {

    @GetMapping("/user/info/{userId}")
    ResponseEntity<Result<Map<String, Object>>> getUser(@PathVariable("userId") Long userId);

    @PostMapping("/user/register")
    Result<String> register(@RequestBody RegisterDTO dto);

    @PutMapping("/address/{id}")
    Result<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressDTO dto);

    @DeleteMapping("/address/{id}")
    Result<?> deleteAddress(@PathVariable("id") Long id);
}