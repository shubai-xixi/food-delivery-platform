package com.elm.service;

import com.elm.common.Result;
import com.elm.dto.CartDTO;
import com.elm.vo.CartVO;

import java.util.List;

public interface CartService {
    Result<?> add(Long userId, CartDTO dto);
    Result<List<CartVO>> list(Long userId, Integer shopId);
    Result<CartVO> updateQuantity(Long userId, Integer cartId, Integer quantity);
    Result<?> delete(Long userId, Integer cartId);
    Result<?> clear(Long userId, Integer shopId);
}