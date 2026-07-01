package com.elm.service;

import com.elm.common.Result;
import com.elm.vo.GoodsVO;

import java.util.List;

public interface GoodsService {
    Result<List<GoodsVO>> listByShop(Integer shopId);

    // 订单服务要用
    Result<GoodsVO> getById(Integer id);

    // 减库存
    Result<?> decreaseStock(Integer id, Integer quantity);
}