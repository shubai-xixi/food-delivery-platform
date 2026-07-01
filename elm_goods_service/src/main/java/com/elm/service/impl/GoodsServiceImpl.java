package com.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.common.Result;
import com.elm.common.ResultCode;
import com.elm.entity.Goods;
import com.elm.entity.GoodsSku;
import com.elm.mapper.GoodsMapper;
import com.elm.mapper.GoodsSkuMapper;
import com.elm.service.GoodsService;
import com.elm.vo.GoodsSkuVO;
import com.elm.vo.GoodsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsSkuMapper goodsSkuMapper;

    @Override
    public Result<List<GoodsVO>> listByShop(Integer shopId) {
        List<Goods> list = goodsMapper.selectList(
                new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getShopId, shopId)
                        .eq(Goods::getStatus, "ON")
        );
        List<GoodsVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        return Result.ok(voList);
    }

    @Override
    public Result<GoodsVO> getById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        return Result.ok(toVO(goods));
    }

    @Override
    @Transactional
    public Result<?> decreaseStock(Integer id, Integer quantity) {
        GoodsSku sku = goodsSkuMapper.selectById(id);
        if (sku == null) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        if (sku.getStock() < quantity) {
            return Result.fail(400, "库存不足");
        }
        sku.setStock(sku.getStock() - quantity);
        goodsSkuMapper.updateById(sku);
        return Result.ok();
    }

    private GoodsVO toVO(Goods goods) {
        List<GoodsSku> skus = goodsSkuMapper.selectList(
                new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getGoodsId, goods.getId()));
        return GoodsVO.builder()
                .id(goods.getId())
                .name(goods.getName())
                .shopId(goods.getShopId())
                .description(goods.getDescription())
                .imageUrl(goods.getImageUrl())
                .options(goods.getOptions())
                .status(goods.getStatus())
                .skus(skus.stream().map(s -> GoodsSkuVO.builder()
                        .id(s.getId())
                        .specName(s.getSpecName())
                        .price(s.getPrice())
                        .stock(s.getStock())
                        .isDefault(s.getIsDefault())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}