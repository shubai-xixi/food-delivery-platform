package com.elm.service;

import com.elm.common.Result;
import com.elm.dto.OrderDTO;
import com.elm.vo.OrderListVO;
import com.elm.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /** 生成订单 */
    Result<OrderVO> create(Long userId, OrderDTO dto);

    /** 支付订单（校验库存 + 扣库存 + 改支付状态） */
    Result<?> pay(Long userId, Integer orderId, String payMethod);

    /** 查看订单列表 */
    Result<List<OrderListVO>> list(Long userId);
    /** 查看订单详情 */
    Result<OrderVO> detail(Long userId, Integer orderId);
}