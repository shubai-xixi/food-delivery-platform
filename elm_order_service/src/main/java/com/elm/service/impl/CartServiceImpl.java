package com.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.common.Result;
import com.elm.common.ResultCode;
import com.elm.dto.CartDTO;
import com.elm.entity.Cart;
import com.elm.feign.GoodsClient;
import com.elm.mapper.CartMapper;
import com.elm.service.CartService;
import com.elm.vo.CartVO;
import com.elm.vo.GoodsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final GoodsClient goodsClient;

    @Override
    @Transactional
    public Result<?> add(Long userId, CartDTO dto) {
        // 1. Feign 查商品，校验 goodsId、skuId、shopId
        Result<GoodsVO> goodsResult = goodsClient.getGoods(dto.getGoodsId());
        if (goodsResult.getCode() != 200 || goodsResult.getData() == null) {
            return Result.fail(400, "商品不存在或已下架");
        }
        GoodsVO goods = goodsResult.getData();

        // 校验 shopId 是否匹配
        if (!goods.getShopId().equals(dto.getShopId())) {
            return Result.fail(400, "商品不属于该商家");
        }

        // 校验 skuId 是否属于该商品
        boolean skuExists = goods.getSkus().stream()
                .anyMatch(s -> s.getId().equals(dto.getSkuId()));
        if (!skuExists) {
            return Result.fail(400, "商品规格不存在");
        }

        // 2. 查重
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getShopId, dto.getShopId())
                .eq(Cart::getGoodsId, dto.getGoodsId())
                .eq(Cart::getSkuId, dto.getSkuId())
                .eq(Cart::getPreferences, dto.getPreferences());

        Cart cart = cartMapper.selectOne(wrapper);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + dto.getQuantity());
            cartMapper.updateById(cart);
        } else {
            cart = Cart.builder()
                    .userId(userId)
                    .shopId(dto.getShopId())
                    .goodsId(dto.getGoodsId())
                    .skuId(dto.getSkuId())
                    .preferences(dto.getPreferences())
                    .quantity(dto.getQuantity())
                    .isChecked(1)
                    .build();
            cartMapper.insert(cart);
        }

        log.info("购物车添加成功 userId={} skuId={}", userId, dto.getSkuId());
        return Result.ok();
    }

    @Override
    public Result<List<CartVO>> list(Long userId, Integer shopId) {
        List<Cart> list = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(shopId != null, Cart::getShopId, shopId)
                        .orderByDesc(Cart::getCreatedAt)
        );
        List<CartVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        return Result.ok(voList);
    }

    @Override
    @Transactional
    public Result<CartVO> updateQuantity(Long userId, Integer cartId, Integer quantity) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        cart.setQuantity(quantity);
        cartMapper.updateById(cart);
        return Result.ok(toVO(cart));
    }

    @Override
    @Transactional
    public Result<?> delete(Long userId, Integer cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        cartMapper.deleteById(cartId);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result<?> clear(Long userId, Integer shopId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId).eq(Cart::getShopId, shopId);
        cartMapper.delete(wrapper);
        return Result.ok();
    }

    private CartVO toVO(Cart cart) {
        return CartVO.builder()
                .id(cart.getId())
                .goodsId(cart.getGoodsId())
                .skuId(cart.getSkuId())
                .preferences(cart.getPreferences())
                .quantity(cart.getQuantity())
                .build();
    }
}