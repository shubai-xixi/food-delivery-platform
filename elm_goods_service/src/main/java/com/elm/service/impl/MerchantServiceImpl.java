package com.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.common.Result;
import com.elm.common.ResultCode;
import com.elm.entity.Merchant;
import com.elm.mapper.MerchantMapper;
import com.elm.service.MerchantService;
import com.elm.vo.MerchantVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;

    @Override
    public Result<List<MerchantVO>> list(Integer categoryId, String sort) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getStatus, "OPEN");

        if (categoryId != null) {
            wrapper.eq(Merchant::getCategoryId, categoryId);
        }

        if ("sales".equals(sort)) {
            wrapper.orderByDesc(Merchant::getMonthlySales);
        } else {
            wrapper.orderByDesc(Merchant::getRating)
                    .orderByDesc(Merchant::getMonthlySales);
        }

        List<MerchantVO> voList = merchantMapper.selectList(wrapper).stream()
                .map(this::toVO).collect(Collectors.toList());
        return Result.ok(voList);
    }

    @Override
    public Result<MerchantVO> detail(Integer merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        return Result.ok(toVO(merchant));
    }

    private MerchantVO toVO(Merchant m) {
        return MerchantVO.builder()
                .id(m.getId())
                .shopName(m.getShopName())
                .categoryId(m.getCategoryId())
                .rating(m.getRating())
                .minPrice(m.getMinPrice())
                .deliveryFee(m.getDeliveryFee())
                .estimatedTime(m.getEstimatedMinTime() + "-" + m.getEstimatedMaxTime() + "分钟")
                .monthlySales(m.getMonthlySales())
                .imageUrl(m.getImageUrl())
                .address(m.getAddress())
                .status(m.getStatus())
                .build();
    }
}