// OrderService.java
package com.elm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.common.Result;
import com.elm.dto.CartDTO;
import com.elm.dto.OrderDTO;
import com.elm.entity.Cart;
import com.elm.entity.Orders;
import com.elm.feign.GoodsFeignClient;
import com.elm.feign.UserFeignClient;
import com.elm.mapper.CartMapper;
import com.elm.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    // 添加购物车
    public void addToCart(Long userId, CartDTO dto) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setShopId(dto.getShopId());
        cart.setGoodsId(dto.getGoodsId());
        cart.setGoodsName(dto.getGoodsName());
        cart.setGoodsImage(dto.getGoodsImage());
        cart.setSelectedSize(dto.getSelectedSize());
        cart.setSelectedTemperature(dto.getSelectedTemperature());
        cart.setSelectedSweetness(dto.getSelectedSweetness());
        cart.setPrice(dto.getPrice());
        cart.setQuantity(dto.getQuantity());
        cartMapper.insert(cart);
    }

    // 查看购物车
    public List<Cart> cartList(Long userId) {
        return cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .orderByDesc(Cart::getCreatedAt)
        );
    }

    // 删除购物车商品
    public void removeFromCart(Integer cartId) {
        cartMapper.deleteById(cartId);
    }

    // 清空用户购物车
    public void clearCart(Long userId) {
        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
    }

    // 下单
    public Orders createOrder(Long userId, OrderDTO dto) {
        // 1. 调用 user-service 验证地址是否存在
        Result<List<Map<String, Object>>> addressResult = userFeignClient.getAddressList(userId);
        if (addressResult.getCode() != 200 || addressResult.getData() == null) {
            throw new RuntimeException("获取收货地址失败");
        }
        boolean addressExists = addressResult.getData().stream()
                .anyMatch(addr -> Long.parseLong(addr.get("id").toString()) == dto.getAddressId());
        if (!addressExists) {
            throw new RuntimeException("收货地址不存在");
        }

        // 2. 调用 goods-service 验证商家是否存在
        Result<Map<String, Object>> shopResult = goodsFeignClient.getShopDetail(dto.getShopId());
        if (shopResult.getCode() != 200 || shopResult.getData() == null) {
            throw new RuntimeException("商家不存在");
        }

        // 3. 保存订单
        Orders order = new Orders();
        order.setUserId(userId);
        order.setShopId(dto.getShopId());
        order.setShopName(dto.getShopName());
        order.setAddressId(dto.getAddressId());
        order.setAddressSnapshot(dto.getAddressSnapshot());
        order.setTotalPrice(dto.getTotalPrice());
        order.setTotalDiscountPrice(dto.getTotalDiscountPrice());
        order.setDeliveryFee(dto.getDeliveryFee());
        order.setActualPay(dto.getActualPay());
        order.setStatus("UNPAID");
        order.setItems(dto.getItems());
        order.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        // 4. 清空购物车
        clearCart(userId);

        return order;
    }

    // 历史订单
    public List<Orders> orderList(Long userId) {
        return orderMapper.selectList(
                new LambdaQueryWrapper<Orders>()
                        .eq(Orders::getUserId, userId)
                        .orderByDesc(Orders::getCreatedAt)
        );
    }

    // 查询单个订单
    public Orders getOrder(Integer orderId) {
        return orderMapper.selectById(orderId);
    }
}