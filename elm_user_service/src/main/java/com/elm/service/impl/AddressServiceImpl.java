package com.elm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.annotation.Log;
import com.elm.common.Result;
import com.elm.common.ResultCode;
import com.elm.dto.AddressDTO;
import com.elm.entity.Address;
import com.elm.mapper.AddressMapper;
import com.elm.service.AddressService;
import com.elm.vo.AddressVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public Result<AddressVO> add(Long userId, AddressDTO dto) {
        long count = addressMapper.selectCount(
                new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId));

        Address address = Address.builder()
                .userId(userId)
                .contactName(dto.getContactName())
                .contactPhone(dto.getContactPhone())
                .province(dto.getProvince())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .detail(dto.getDetail())
                .label(dto.getLabel())
                .isDefault(count == 0 ? 1 : 0)
                .build();

        addressMapper.insert(address);
        log.info("新增地址成功 userId={} addressId={} isDefault={}", userId, address.getId(), address.getIsDefault());
        return Result.ok(toVO(address));
    }

    @Log("删除地址")
    @Override
    @Transactional
    public Result<?> delete(Long userId, Long addressId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }

        boolean wasDefault = address.getIsDefault() == 1;
        addressMapper.deleteById(addressId);
        log.info("删除地址成功 userId={} addressId={}", userId, addressId);

        if (wasDefault) {
            setFirstAsDefault(userId);
        }
        return Result.ok();
    }

    @Override
    @Transactional
    public Result<AddressVO> update(Long userId, Long addressId, AddressDTO dto) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }

        if (dto.getContactName() != null) address.setContactName(dto.getContactName());
        if (dto.getContactPhone() != null) address.setContactPhone(dto.getContactPhone());
        if (dto.getProvince() != null) address.setProvince(dto.getProvince());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getDistrict() != null) address.setDistrict(dto.getDistrict());
        if (dto.getDetail() != null) address.setDetail(dto.getDetail());
        if (dto.getLabel() != null) address.setLabel(dto.getLabel());

        addressMapper.updateById(address);
        log.info("修改地址成功 userId={} addressId={}", userId, addressId);
        return Result.ok(toVO(address));
    }

    @Override
    public Result<List<AddressVO>> list(Long userId) {
        List<Address> list = addressMapper.selectList(
                new LambdaQueryWrapper<Address>()
                        .eq(Address::getUserId, userId)
                        .orderByDesc(Address::getIsDefault)
                        .orderByDesc(Address::getCreatedAt));
        List<AddressVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        return Result.ok(voList);
    }

    @Override
    @Transactional
    public Result<?> setDefault(Long userId, Long addressId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }

        cancelDefault(userId);
        address.setIsDefault(1);
        addressMapper.updateById(address);
        log.info("设置默认地址成功 userId={} addressId={}", userId, addressId);
        return Result.ok();
    }

    @Override
    public Result<AddressVO> getById(Long userId, Long addressId) {
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.fail(ResultCode.NOT_FOUND);
        }
        return Result.ok(toVO(address));
    }

    private void cancelDefault(Long userId) {
        List<Address> defaultList = addressMapper.selectList(
                new LambdaQueryWrapper<Address>()
                        .eq(Address::getUserId, userId)
                        .eq(Address::getIsDefault, 1));
        for (Address addr : defaultList) {
            addr.setIsDefault(0);
            addressMapper.updateById(addr);
        }
    }

    private void setFirstAsDefault(Long userId) {
        List<Address> list = addressMapper.selectList(
                new LambdaQueryWrapper<Address>()
                        .eq(Address::getUserId, userId)
                        .orderByDesc(Address::getCreatedAt)
                        .last("LIMIT 1"));
        if (!list.isEmpty()) {
            Address first = list.get(0);
            first.setIsDefault(1);
            addressMapper.updateById(first);
        }
    }

    private AddressVO toVO(Address address) {
        return AddressVO.builder()
                .id(address.getId())
                .contactName(address.getContactName())
                .contactPhone(address.getContactPhone())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .detail(address.getDetail())
                .label(address.getLabel())
                .isDefault(address.getIsDefault())
                .build();
    }
}