package com.elm.feign;

import com.elm.common.Result;
import com.elm.vo.GoodsVO;
import com.elm.vo.MerchantVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "elm-goods-service")
public interface GoodsClient {

    @GetMapping("/goods/{id}")
    Result<GoodsVO> getGoods(@PathVariable Integer id);

    @GetMapping("/merchants/{id}")
    Result<MerchantVO> getMerchant(@PathVariable Integer id);

    @PutMapping("/goods/{id}/stock/decrease")
    Result<?> decreaseStock(@PathVariable Integer id, @RequestParam Integer quantity);
}
