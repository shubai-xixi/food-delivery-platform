// AddressService.java
package com.elm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elm.dto.AddressDTO;
import com.elm.entity.Address;
import com.elm.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public Address add(Long userId, AddressDTO dto) {
        Address address = new Address();
        address.setUserId(userId);
        address.setContactName(dto.getContactName());
        address.setContactPhone(dto.getContactPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        address.setLabel(dto.getLabel());

        // 如果是第一个地址，自动设为默认
        long count = addressMapper.selectCount(
                new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId)
        );
        if (count == 0) {
            address.setIsDefault(1);
        }

        addressMapper.insert(address);
        return address;
    }

    public List<Address> list(Long userId) {
        return addressMapper.selectList(
                new LambdaQueryWrapper<Address>()
                        .eq(Address::getUserId, userId)
                        .orderByDesc(Address::getIsDefault)
                        .orderByDesc(Address::getCreatedAt)
        );
    }

    public Address update(Long id, AddressDTO dto) {
        Address address = addressMapper.selectById(id);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }
        address.setContactName(dto.getContactName());
        address.setContactPhone(dto.getContactPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        address.setLabel(dto.getLabel());
        addressMapper.updateById(address);
        return address;
    }

    public void delete(Long id) {
        addressMapper.deleteById(id);
    }

    public void setDefault(Long userId, Long addressId) {
        // 先把该用户所有地址取消默认
        List<Address> list = addressMapper.selectList(
                new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId)
        );
        for (Address addr : list) {
            addr.setIsDefault(0);
            addressMapper.updateById(addr);
        }
        // 再设置目标地址为默认
        Address target = addressMapper.selectById(addressId);
        if (target != null) {
            target.setIsDefault(1);
            addressMapper.updateById(target);
        }
    }
}