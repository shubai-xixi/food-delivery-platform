package com.elm.controller;

import com.elm.common.Result;
import com.elm.common.UserContext;
import com.elm.dto.CartDTO;
import com.elm.service.CartService;
import com.elm.vo.CartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车", description = "购物车增删改查")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping
    public Result<?> add(@RequestBody CartDTO dto) {
        Long userId = UserContext.getUserId();
        return cartService.add(userId, dto);
    }

    @Operation(summary = "查看购物车（按商家）")
    @GetMapping
    public Result<List<CartVO>> list(@RequestParam(required = false) Integer shopId) {
        Long userId = UserContext.getUserId();
        return cartService.list(userId, shopId);
    }

    @Operation(summary = "修改数量")
    @PutMapping("/{id}")
    public Result<CartVO> updateQuantity(@PathVariable Integer id, @RequestParam Integer quantity) {
        Long userId = UserContext.getUserId();
        return cartService.updateQuantity(userId, id, quantity);
    }

    @Operation(summary = "删除购物车项")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        Long userId = UserContext.getUserId();
        return cartService.delete(userId, id);
    }

    @Operation(summary = "清空商家购物车")
    @DeleteMapping("/clear")
    public Result<?> clear(@RequestParam Integer shopId) {
        Long userId = UserContext.getUserId();
        return cartService.clear(userId, shopId);
    }
}