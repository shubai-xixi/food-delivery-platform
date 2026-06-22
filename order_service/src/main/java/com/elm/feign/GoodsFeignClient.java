// GoodsFeignClient.java
package com.elm.feign;

import com.elm.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name = "goods-service")
public interface GoodsFeignClient {

    @GetMapping("/goods/shop/{id}")
    Result<Map<String, Object>> getShopDetail(@PathVariable("id") Integer id);

    @GetMapping("/goods/list")
    Result<java.util.List<Map<String, Object>>> getGoodsList(@RequestParam("shopId") Integer shopId);
}