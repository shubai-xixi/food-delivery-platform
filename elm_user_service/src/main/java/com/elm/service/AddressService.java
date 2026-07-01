package com.elm.service;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.vo.AddressVO;

import java.util.List;

public interface AddressService {

    /** 新增地址 */
    Result<AddressVO> add(Long userId, AddressDTO dto);

    /** 删除地址 */
    Result<?> delete(Long userId, Long addressId);

    /** 修改地址 */
    Result<AddressVO> update(Long userId, Long addressId, AddressDTO dto);

    /** 查询用户所有地址 */
    Result<List<AddressVO>> list(Long userId);

    /** 设置默认地址 */
    Result<?> setDefault(Long userId, Long addressId);

    // 订单服务要用
    Result<AddressVO> getById(Long userId, Long addressId);
}