// GoodsService.java
package com.elm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.entity.Category;
import com.elm.entity.Goods;
import com.elm.entity.Merchant;
import com.elm.mapper.CategoryMapper;
import com.elm.mapper.GoodsMapper;
import com.elm.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsService {

    @Autowired private CategoryMapper categoryMapper;
    @Autowired private MerchantMapper merchantMapper;
    @Autowired private GoodsMapper goodsMapper;

    public List<Category> categoryList() {
        return categoryMapper.selectList(null);
    }

    public List<Merchant> shopList(Integer categoryId) {
        return merchantMapper.selectList(
                new LambdaQueryWrapper<Merchant>().eq(Merchant::getCategoryId, categoryId)
        );
    }

    public Merchant shopDetail(Integer shopId) {
        return merchantMapper.selectById(shopId);
    }

    public List<Goods> goodsList(Integer shopId) {
        return goodsMapper.selectList(
                new LambdaQueryWrapper<Goods>().eq(Goods::getShopId, shopId)
        );
    }
}