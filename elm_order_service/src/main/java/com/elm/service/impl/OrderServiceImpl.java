package com.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.common.Result;
import com.elm.common.ResultCode;
import com.elm.dto.OrderDTO;
import com.elm.entity.Cart;
import com.elm.entity.Order;
import com.elm.entity.OrderItem;
import com.elm.exception.BusinessException;
import com.elm.feign.GoodsClient;
import com.elm.feign.UserClient;
import com.elm.mapper.CartMapper;
import com.elm.mapper.OrderMapper;
import com.elm.mapper.OrderItemMapper;
import com.elm.service.OrderService;
import com.elm.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ObjectMapper objectMapper;
    private final GoodsClient goodsClient;
    private final UserClient userClient;

    // ==================== 生成订单 ====================

    @Override
    @Transactional
    public Result<OrderVO> create(Long userId, OrderDTO dto) {
        // 1. 查购物车
        List<Cart> cartList = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getShopId, dto.getShopId()));
        if (cartList.isEmpty()) {
            return Result.fail(400, "购物车为空");
        }

        // 2. 校验地址
        Result<AddressVO> addrResult = userClient.getAddress(dto.getAddressId());
        if (addrResult.getCode() != 200 || addrResult.getData() == null) {
            return Result.fail(400, "收货地址不存在");
        }
        AddressVO address = addrResult.getData();
        String addressSnapshot = address.getProvince() + address.getCity() + address.getDistrict()
                + address.getDetail() + "，" + address.getContactName() + "，" + address.getContactPhone();

        // 3. 查商家
        Result<MerchantVO> merchantResult = goodsClient.getMerchant(dto.getShopId());
        if (merchantResult.getCode() != 200 || merchantResult.getData() == null) {
            return Result.fail(400, "商家不存在");
        }
        MerchantVO merchant = merchantResult.getData();

        // 4. 逐条校验 + 计算金额
        float totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemVO> itemVOs = new ArrayList<>();

        for (Cart cart : cartList) {
            // 查商品
            Result<GoodsVO> goodsResult = goodsClient.getGoods(cart.getGoodsId());
            if (goodsResult.getCode() != 200 || goodsResult.getData() == null) {
                return Result.fail(400, "商品【" + cart.getGoodsId() + "】已下架或不存在");
            }
            GoodsVO goods = goodsResult.getData();

            // 查 SKU
            GoodsSkuVO sku = goods.getSkus().stream()
                    .filter(s -> s.getId().equals(cart.getSkuId()))
                    .findFirst().orElse(null);
            if (sku == null) {
                return Result.fail(400, "商品【" + goods.getName() + "】的规格已变更，请重新选择");
            }

            // 校验偏好
            validatePreferences(cart.getPreferences(), goods.getOptions(), sku.getSpecName());

            // 计算小料加价
            float addonPrice = calcAddonPrice(cart.getPreferences(), goods.getOptions());
            float finalPrice = sku.getPrice() + addonPrice;
            int quantity = cart.getQuantity();

            OrderItem item = OrderItem.builder()
                    .goodsId(cart.getGoodsId())
                    .goodsName(goods.getName())
                    .skuId(cart.getSkuId())
                    .specName(sku.getSpecName())
                    .basePrice(sku.getPrice())
                    .preferences(cart.getPreferences())
                    .addonPrice(addonPrice)
                    .finalPrice(finalPrice)
                    .quantity(quantity)
                    .build();
            orderItems.add(item);
            totalPrice += finalPrice * quantity;

            itemVOs.add(OrderItemVO.builder()
                    .goodsName(goods.getName())
                    .specName(sku.getSpecName())
                    .preferences(cart.getPreferences())
                    .basePrice(sku.getPrice())
                    .addonPrice(addonPrice)
                    .finalPrice(finalPrice)
                    .quantity(quantity)
                    .build());
        }

        float deliveryFee = merchant.getDeliveryFee();
        float actualPay = totalPrice + deliveryFee;

        // 5. 生成订单
        Order order = Order.builder()
                .userId(userId)
                .shopId(dto.getShopId())
                .shopName(merchant.getShopName())
                .addressId(dto.getAddressId())
                .addressSnapshot(addressSnapshot)
                .totalPrice(totalPrice)
                .totalDiscountPrice(totalPrice)
                .deliveryFee(deliveryFee)
                .actualPay(actualPay)
                .orderType("NORMAL")
                .payStatus("UNPAID")
                .status("PENDING")
                .build();
        orderMapper.insert(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        // 6. 清空购物车
        cartMapper.delete(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getShopId, dto.getShopId()));

        log.info("订单创建成功 orderId={} userId={} amount={}", order.getId(), userId, actualPay);
        return Result.ok(OrderVO.builder()
                .id(order.getId())
                .shopName(merchant.getShopName())
                .addressSnapshot(addressSnapshot)
                .totalPrice(totalPrice)
                .deliveryFee(deliveryFee)
                .actualPay(actualPay)
                .payStatus("UNPAID")
                .status("PENDING")
                .items(itemVOs)
                .build());
    }

    // ==================== 支付订单 ====================

    @Override
    @Transactional
    public Result<?> pay(Long userId, Integer orderId, String payMethod) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        if (!"UNPAID".equals(order.getPayStatus())) {
            return Result.fail(400, "订单状态不正确");
        }

        // 扣库存
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : items) {
            Result<?> stockResult = goodsClient.decreaseStock(item.getSkuId(), item.getQuantity());
            if (stockResult.getCode() != 200) {
                // 库存不足，支付失败
                order.setPayStatus("FAIL");
                order.setPayMethod(payMethod);
                orderMapper.updateById(order);
                return Result.fail(400, "商品【" + item.getGoodsName() + "】" + stockResult.getMsg());
            }
        }

        // 支付成功
        order.setPayStatus("PAID");
        order.setPayMethod(payMethod);
        order.setPayTime(LocalDateTime.now());
        order.setPayTransactionNo("PAY" + System.currentTimeMillis());
        order.setStatus("CONFIRMED");
        orderMapper.updateById(order);

        log.info("支付成功 orderId={} userId={} method={}", orderId, userId, payMethod);
        return Result.ok();
    }

    // ==================== 查看订单列表 ====================

    @Override
    public Result<List<OrderListVO>> list(Long userId) {
        List<Order> orders = orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getUserId, userId)
                        .orderByDesc(Order::getCreatedAt));
        List<OrderListVO> voList = orders.stream().map(o -> OrderListVO.builder()
                .id(o.getId())
                .shopName(o.getShopName())
                .addressSnapshot(o.getAddressSnapshot())
                .totalPrice(o.getTotalPrice())
                .deliveryFee(o.getDeliveryFee())
                .actualPay(o.getActualPay())
                .payStatus(o.getPayStatus())
                .status(o.getStatus())
                .createdAt(o.getCreatedAt() != null ? o.getCreatedAt().toString() : null)
                .build()).collect(Collectors.toList());
        return Result.ok(voList);
    }

    // ==================== 查看订单详情 ====================

    @Override
    public Result<OrderVO> detail(Long userId, Integer orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        return Result.ok(toVO(order));
    }

    // ==================== 工具方法 ====================

    private OrderVO toVO(Order order) {
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        List<OrderItemVO> itemVOs = items.stream().map(i -> OrderItemVO.builder()
                .goodsName(i.getGoodsName())
                .specName(i.getSpecName())
                .preferences(i.getPreferences())
                .basePrice(i.getBasePrice())
                .addonPrice(i.getAddonPrice())
                .finalPrice(i.getFinalPrice())
                .quantity(i.getQuantity())
                .build()).collect(Collectors.toList());
        return OrderVO.builder()
                .id(order.getId())
                .shopName(order.getShopName())
                .addressSnapshot(order.getAddressSnapshot())
                .totalPrice(order.getTotalPrice())
                .deliveryFee(order.getDeliveryFee())
                .actualPay(order.getActualPay())
                .payStatus(order.getPayStatus())
                .status(order.getStatus())
                .items(itemVOs)
                .build();
    }

    @SuppressWarnings("unchecked")
    private void validatePreferences(String preferences, String options, String specName) {
        if (preferences == null || preferences.isEmpty()) return;
        try {
            Map<String, Object> prefMap = objectMapper.readValue(preferences, Map.class);
            Map<String, Object> optMap = options != null ? objectMapper.readValue(options, Map.class) : Map.of();

            // 白名单 = options 的 key + "规格"
            List<String> allowedKeys = new ArrayList<>(optMap.keySet());
            allowedKeys.add("规格");

            // 检查未知字段
            for (String key : prefMap.keySet()) {
                if (!allowedKeys.contains(key)) {
                    throw new RuntimeException("不合法的偏好字段：" + key);
                }
            }

            // 规格：传了必须匹配 SKU
            if (prefMap.containsKey("规格")) {
                String spec = (String) prefMap.get("规格");
                if (!spec.equals(specName))
                    throw new BusinessException("规格不匹配，期望：" + specName + "，实际：" + spec);
            }

            // 遍历 options 里的 key，逐个校验值
            for (String key : optMap.keySet()) {
                if (!prefMap.containsKey(key)) continue;
                Object optValue = optMap.get(key);

                if (optValue instanceof List) {
                    // 温度、甜度这类：值必须在列表中
                    String userValue = (String) prefMap.get(key);
                    List<String> validList = (List<String>) optValue;
                    if (!validList.contains(userValue))
                        throw new BusinessException(key + "选项不合法：" + userValue);
                } else if (optValue instanceof Map) {
                    // 小料这类：每个 key 必须在 options 的 map 里
                    List<String> userAddons = (List<String>) prefMap.get(key);
                    Map<String, Object> validMap = (Map<String, Object>) optValue;
                    for (String a : userAddons) {
                        if (!validMap.containsKey(a))
                            throw new RuntimeException(key + "不合法：" + a);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("偏好格式错误");
        }
    }

    @SuppressWarnings("unchecked")
    private float calcAddonPrice(String preferences, String options) {
        if (preferences == null || options == null) return 0f;
        try {
            Map<String, Object> prefMap = objectMapper.readValue(preferences, Map.class);
            Map<String, Object> optMap = objectMapper.readValue(options, Map.class);
            float total = 0f;
            for (String key : optMap.keySet()) {
                Object optValue = optMap.get(key);
                if (optValue instanceof Map && prefMap.containsKey(key)) {
                    List<String> userAddons = (List<String>) prefMap.get(key);
                    Map<String, Object> priceMap = (Map<String, Object>) optValue;
                    for (String a : userAddons) {
                        if (priceMap.containsKey(a)) {
                            total += ((Number) priceMap.get(a)).floatValue();
                        }
                    }
                }
            }
            return total;
        } catch (Exception e) {
            return 0f;
        }
    }

}

//    @SuppressWarnings("unchecked")
//    private void validatePreferences(String preferences, String options) {
//        if (preferences == null || options == null) return;
//        try {
//            Map<String, Object> prefMap = objectMapper.readValue(preferences, Map.class);
//            Map<String, Object> optMap = objectMapper.readValue(options, Map.class);
//
//            if (prefMap.containsKey("温度") && optMap.containsKey("温度")) {
//                String temp = (String) prefMap.get("温度");
//                List<String> tempList = (List<String>) optMap.get("温度");
//                if (!tempList.contains(temp)) throw new RuntimeException("温度选项不合法：" + temp);
//            }
//            if (prefMap.containsKey("甜度") && optMap.containsKey("甜度")) {
//                String sweet = (String) prefMap.get("甜度");
//                List<String> sweetList = (List<String>) optMap.get("甜度");
//                if (!sweetList.contains(sweet)) throw new RuntimeException("甜度选项不合法：" + sweet);
//            }
//            if (prefMap.containsKey("小料") && optMap.containsKey("小料")) {
//                List<String> addons = (List<String>) prefMap.get("小料");
//                Map<String, Object> addonMap = (Map<String, Object>) optMap.get("小料");
//                for (String a : addons) {
//                    if (!addonMap.containsKey(a)) throw new RuntimeException("小料不合法：" + a);
//                }
//            }
//        } catch (RuntimeException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException("偏好格式错误");
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private float calcAddonPrice(String preferences, String options) {
//        if (preferences == null || options == null) return 0f;
//        try {
//            Map<String, Object> prefMap = objectMapper.readValue(preferences, Map.class);
//            Map<String, Object> optMap = objectMapper.readValue(options, Map.class);
//            if (prefMap.containsKey("小料") && optMap.containsKey("小料")) {
//                List<String> addons = (List<String>) prefMap.get("小料");
//                Map<String, Object> addonMap = (Map<String, Object>) optMap.get("小料");
//                float total = 0f;
//                for (String a : addons) {
//                    if (addonMap.containsKey(a)) {
//                        total += ((Number) addonMap.get(a)).floatValue();
//                    }
//                }
//                return total;
//            }
//        } catch (Exception ignored) {}
//        return 0f;
//    }
//}

//package com.elm.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.elm.common.Result;
//import com.elm.common.ResultCode;
//import com.elm.dto.OrderDTO;
//import com.elm.entity.Cart;
//import com.elm.entity.Order;
//import com.elm.entity.OrderItem;
//import com.elm.mapper.CartMapper;
//import com.elm.mapper.OrderMapper;
//import com.elm.mapper.OrderItemMapper;
//import com.elm.service.OrderService;
//import com.elm.vo.OrderItemVO;
//import com.elm.vo.OrderVO;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class OrderServiceImpl implements OrderService {
//
//    private final CartMapper cartMapper;
//    private final OrderMapper orderMapper;
//    private final OrderItemMapper orderItemMapper;
//    private final ObjectMapper objectMapper;
//
//    // ==================== 生成订单 ====================
//
//    @Override
//    @Transactional
//    public Result<OrderVO> create(Long userId, OrderDTO dto) {
//        // 1. 查购物车
//        List<Cart> cartList = cartMapper.selectList(
//                new LambdaQueryWrapper<Cart>()
//                        .eq(Cart::getUserId, userId)
//                        .eq(Cart::getShopId, dto.getShopId()));
//        if (cartList.isEmpty()) {
//            return Result.fail(400, "购物车为空");
//        }
//
//        // 2. TODO: Feign 校验地址
//        // 3. TODO: Feign 查商家
//
//        // 4. 计算金额
//        float totalPrice = 0;
//        List<OrderItem> orderItems = new ArrayList<>();
//        List<OrderItemVO> itemVOs = new ArrayList<>();
//
//        for (Cart cart : cartList) {
//            // TODO: Feign 查 goods + sku + options，校验 preferences
//            float basePrice = 25f;
//            float addonPrice = 0f;
//            float finalPrice = basePrice + addonPrice;
//            int quantity = cart.getQuantity();
//
//            OrderItem item = OrderItem.builder()
//                    .goodsId(cart.getGoodsId())
//                    .goodsName("商品名待Feign")
//                    .skuId(cart.getSkuId())
//                    .specName("规格待Feign")
//                    .basePrice(basePrice)
//                    .preferences(cart.getPreferences())
//                    .addonPrice(addonPrice)
//                    .finalPrice(finalPrice)
//                    .quantity(quantity)
//                    .build();
//            orderItems.add(item);
//            totalPrice += finalPrice * quantity;
//
//            itemVOs.add(OrderItemVO.builder()
//                    .goodsName(item.getGoodsName())
//                    .specName(item.getSpecName())
//                    .preferences(item.getPreferences())
//                    .basePrice(basePrice)
//                    .addonPrice(addonPrice)
//                    .finalPrice(finalPrice)
//                    .quantity(quantity)
//                    .build());
//        }
//
//        float deliveryFee = 3f;
//        float actualPay = totalPrice + deliveryFee;
//
//        // 5. 生成订单
//        Order order = Order.builder()
//                .userId(userId)
//                .shopId(dto.getShopId())
//                .shopName("商家待Feign")
//                .addressId(dto.getAddressId())
//                .addressSnapshot("地址快照待Feign")
//                .totalPrice(totalPrice)
//                .totalDiscountPrice(totalPrice)
//                .deliveryFee(deliveryFee)
//                .actualPay(actualPay)
//                .orderType("NORMAL")
//                .payStatus("UNPAID")
//                .status("PENDING")
//                .build();
//        orderMapper.insert(order);
//
//        for (OrderItem item : orderItems) {
//            item.setOrderId(order.getId());
//            orderItemMapper.insert(item);
//        }
//
//        // 6. 清空购物车
//        cartMapper.delete(new LambdaQueryWrapper<Cart>()
//                .eq(Cart::getUserId, userId)
//                .eq(Cart::getShopId, dto.getShopId()));
//
//        log.info("订单创建成功 orderId={} userId={} amount={}", order.getId(), userId, actualPay);
//        return Result.ok(OrderVO.builder()
//                .id(order.getId())
//                .shopName(order.getShopName())
//                .addressSnapshot(order.getAddressSnapshot())
//                .totalPrice(totalPrice)
//                .deliveryFee(deliveryFee)
//                .actualPay(actualPay)
//                .payStatus("UNPAID")
//                .status("PENDING")
//                .items(itemVOs)
//                .build());
//    }
//
//    // ==================== 支付订单 ====================
//
//    @Override
//    @Transactional
//    public Result<?> pay(Long userId, Integer orderId) {
//        Order order = orderMapper.selectById(orderId);
//        if (order == null || !order.getUserId().equals(userId)) {
//            return Result.fail(ResultCode.NOT_FOUND);
//        }
//        if (!"UNPAID".equals(order.getPayStatus())) {
//            return Result.fail(400, "订单已支付或已取消");
//        }
//
//        // TODO: Feign 批量校验并扣减库存
//        // List<OrderItem> items = orderItemMapper.selectList(
//        //     new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
//        // for (OrderItem item : items) {
//        //     goodsClient.decreaseStock(item.getSkuId(), item.getQuantity());
//        // }
//
//        // 更新支付状态
//        order.setPayStatus("PAID");
//        order.setPayMethod("SIMULATE");
//        order.setPayTime(LocalDateTime.now());
//        order.setPayTransactionNo("PAY" + System.currentTimeMillis());
//        order.setStatus("CONFIRMED");
//        orderMapper.updateById(order);
//
//        log.info("支付成功 orderId={} userId={}", orderId, userId);
//        return Result.ok();
//    }
//
//    // ==================== 查看订单列表 ====================
//
//    @Override
//    public Result<List<OrderVO>> list(Long userId) {
//        List<Order> orders = orderMapper.selectList(
//                new LambdaQueryWrapper<Order>()
//                        .eq(Order::getUserId, userId)
//                        .orderByDesc(Order::getCreatedAt));
//        List<OrderVO> voList = orders.stream().map(this::toVO).collect(Collectors.toList());
//        return Result.ok(voList);
//    }
//
//    // ==================== 查看订单详情 ====================
//
//    @Override
//    public Result<OrderVO> detail(Long userId, Integer orderId) {
//        Order order = orderMapper.selectById(orderId);
//        if (order == null || !order.getUserId().equals(userId)) {
//            return Result.fail(ResultCode.NOT_FOUND);
//        }
//        return Result.ok(toVO(order));
//    }
//
//    // ==================== 工具方法 ====================
//
//    private OrderVO toVO(Order order) {
//        List<OrderItem> items = orderItemMapper.selectList(
//                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
//        List<OrderItemVO> itemVOs = items.stream().map(i -> OrderItemVO.builder()
//                .goodsName(i.getGoodsName())
//                .specName(i.getSpecName())
//                .preferences(i.getPreferences())
//                .basePrice(i.getBasePrice())
//                .addonPrice(i.getAddonPrice())
//                .finalPrice(i.getFinalPrice())
//                .quantity(i.getQuantity())
//                .build()).collect(Collectors.toList());
//
//        return OrderVO.builder()
//                .id(order.getId())
//                .shopName(order.getShopName())
//                .addressSnapshot(order.getAddressSnapshot())
//                .totalPrice(order.getTotalPrice())
//                .deliveryFee(order.getDeliveryFee())
//                .actualPay(order.getActualPay())
//                .payStatus(order.getPayStatus())
//                .status(order.getStatus())
//                .items(itemVOs)
//                .build();
//    }
//
//    @SuppressWarnings("unchecked")
//    private void validatePreferences(String preferences, String options) {
//        if (preferences == null || options == null) return;
//        try {
//            Map<String, Object> prefMap = objectMapper.readValue(preferences, Map.class);
//            Map<String, Object> optMap = objectMapper.readValue(options, Map.class);
//            if (prefMap.containsKey("温度") && optMap.containsKey("温度")) {
//                List<String> list = (List<String>) optMap.get("温度");
//                if (!list.contains((String) prefMap.get("温度")))
//                    throw new RuntimeException("温度不合法");
//            }
//            if (prefMap.containsKey("甜度") && optMap.containsKey("甜度")) {
//                List<String> list = (List<String>) optMap.get("甜度");
//                if (!list.contains((String) prefMap.get("甜度")))
//                    throw new RuntimeException("甜度不合法");
//            }
//            if (prefMap.containsKey("小料") && optMap.containsKey("小料")) {
//                List<String> addons = (List<String>) prefMap.get("小料");
//                Map<String, Object> addonMap = (Map<String, Object>) optMap.get("小料");
//                for (String a : addons)
//                    if (!addonMap.containsKey(a))
//                        throw new RuntimeException("小料不合法：" + a);
//            }
//        } catch (RuntimeException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException("偏好格式错误");
//        }
//    }
//}