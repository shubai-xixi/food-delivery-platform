package com.elm.feign;

import com.elm.common.Result;
import com.elm.vo.AddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "elm-user-service")
public interface UserClient {

    @GetMapping("/address/{id}")
    Result<AddressVO> getAddress(@PathVariable Long id);
}