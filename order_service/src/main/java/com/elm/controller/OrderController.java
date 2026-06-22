// OrderController.java
package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.CartDTO;
import com.elm.dto.OrderDTO;
import com.elm.entity.Cart;
import com.elm.entity.Orders;
import com.elm.feign.GoodsFeignClient;
import com.elm.feign.UserFeignClient;
import com.elm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private GoodsFeignClient goodsFeignClient;

    // 添加购物车
    @PostMapping("/cart/add")
    public Result<?> addToCart(@RequestHeader("X-User-Id") Long userId,
                               @RequestBody CartDTO dto) {
        orderService.addToCart(userId, dto);
        return Result.ok();
    }

    // 查看购物车
    @GetMapping("/cart/list")
    public Result<List<Cart>> cartList(@RequestHeader("X-User-Id") Long userId) {
        return Result.ok(orderService.cartList(userId));
    }

    // 删除购物车商品
    @DeleteMapping("/cart/{id}")
    public Result<?> removeFromCart(@PathVariable Integer id) {
        orderService.removeFromCart(id);
        return Result.ok();
    }

    // 清空购物车
    @DeleteMapping("/cart/clear")
    public Result<?> clearCart(@RequestHeader("X-User-Id") Long userId) {
        orderService.clearCart(userId);
        return Result.ok();
    }

    // 下单
    @PostMapping("/create")
    public Result<Orders> createOrder(@RequestHeader("X-User-Id") Long userId,
                                      @RequestBody OrderDTO dto) {
        return Result.ok(orderService.createOrder(userId, dto));
    }

    // 历史订单
    @GetMapping("/list")
    public Result<List<Orders>> orderList(@RequestHeader("X-User-Id") Long userId) {
        return Result.ok(orderService.orderList(userId));
    }

    // 查询单个订单
    @GetMapping("/{id}")
    public Result<Orders> getOrder(@PathVariable Integer id) {
        return Result.ok(orderService.getOrder(id));
    }

    // OrderController.java 加一个测试接口，直观展示服务间调用

    @GetMapping("/test-feign/{userId}")
    public Result<Map<String, Object>> testFeign(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();

        // 1. 调用 user-service 查用户信息
        Result<Map<String, Object>> userResult = userFeignClient.getUserInfo(userId);
        result.put("userInfo", userResult.getData());
        System.out.println("========== 调用 user-service 成功 ==========");
        System.out.println("返回数据：" + userResult.getData());

        // 2. 调用 user-service 查地址列表
        Result<List<Map<String, Object>>> addressResult = userFeignClient.getAddressList(userId);
        result.put("addressList", addressResult.getData());
        System.out.println("========== 调用 user-service 地址接口成功 ==========");
        System.out.println("返回数据：" + addressResult.getData());

        // 3. 调用 goods-service 查商家（假设shopId=1）
        Result<Map<String, Object>> shopResult = goodsFeignClient.getShopDetail(1000);
        result.put("shopInfo", shopResult.getData());
        System.out.println("========== 调用 goods-service 商家接口成功 ==========");
        System.out.println("返回数据：" + shopResult.getData());

        // 4. 调用 goods-service 查商品列表（假设shopId=1）
        Result<List<Map<String, Object>>> goodsResult = goodsFeignClient.getGoodsList(1000);
        result.put("goodsList", goodsResult.getData());
        System.out.println("========== 调用 goods-service 商品接口成功 ==========");
        System.out.println("返回数据：" + goodsResult.getData());

        System.out.println("========== 全部服务间调用完成！ ==========");
        return Result.ok(result);
    }

}