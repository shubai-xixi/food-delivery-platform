package com.elm.controlller;

import com.elm.common.Result;
import com.elm.service.CategoryService;
import com.elm.service.GoodsService;
import com.elm.service.MerchantService;
import com.elm.vo.CategoryVO;
import com.elm.vo.GoodsVO;
import com.elm.vo.MerchantVO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品服务", description = "分类、商家、商品查询")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GoodsController {

    private final CategoryService categoryService;
    private final MerchantService merchantService;
    private final GoodsService goodsService;

    @Operation(summary = "获取所有分类")
    @GetMapping("/categories")
    public Result<List<CategoryVO>> categories() {
        return categoryService.list();
    }

    @Operation(summary = "商家列表")
    @GetMapping("/merchants")
    public Result<List<MerchantVO>> merchants(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String sort) {
        return merchantService.list(categoryId, sort);
    }

    @Operation(summary = "商家详情")
    @GetMapping("/merchants/{id}")
    @CircuitBreaker(name = "goodsService", fallbackMethod = "merchantFallback")
    public Result<MerchantVO> merchantDetail(@PathVariable Integer id) {
        return merchantService.detail(id);
    }

    public Result<MerchantVO> merchantFallback(Integer id, Throwable t) {
        log.warn("商家查询熔断 id={}", id);
        return Result.fail(503, "商品服务繁忙，请稍后重试");
    }

    @Operation(summary = "商家商品列表")
    @GetMapping("/merchants/{shopId}/goods")
    public Result<List<GoodsVO>> goodsList(@PathVariable Integer shopId) {
        return goodsService.listByShop(shopId);
    }

    @Operation(summary = "根据ID查商品")
    @GetMapping("/goods/{id}")
    @CircuitBreaker(name = "goodsService", fallbackMethod = "goodsFallback")
    public Result<GoodsVO> getById(@PathVariable Integer id) {
        return goodsService.getById(id);
    }

    public Result<GoodsVO> goodsFallback(Integer id, Throwable t) {
        log.warn("商品查询熔断 id={}", id);
        return Result.fail(503, "商品服务繁忙，请稍后重试");
    }

    @Operation(summary = "扣减库存（内部调用）")
    @PutMapping("/goods/{id}/stock/decrease")
    @CircuitBreaker(name = "goodsService", fallbackMethod = "stockFallback")
    public Result<?> decreaseStock(@PathVariable Integer id, @RequestParam Integer quantity) {
        return goodsService.decreaseStock(id, quantity);
    }

    public Result<?> stockFallback(Integer id, Integer quantity, Throwable t) {
        log.warn("扣库存熔断 id={}", id);
        return Result.fail(503, "商品服务繁忙，请稍后重试");
    }

}