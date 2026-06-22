// GoodsController.java
package com.elm.controller;

import com.elm.common.Result;
import com.elm.entity.Category;
import com.elm.entity.Goods;
import com.elm.entity.Merchant;
import com.elm.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/category/list")
    public Result<List<Category>> categoryList() {
        return Result.ok(goodsService.categoryList());
    }

    @GetMapping("/shop/list")
    public Result<List<Merchant>> shopList(@RequestParam Integer categoryId) {
        return Result.ok(goodsService.shopList(categoryId));
    }

    @GetMapping("/shop/{id}")
    public Result<Merchant> shopDetail(@PathVariable Integer id) {
        return Result.ok(goodsService.shopDetail(id));
    }

    @GetMapping("/list")
    public Result<List<Goods>> goodsList(@RequestParam Integer shopId) {
        return Result.ok(goodsService.goodsList(shopId));
    }
}