package com.elm.service;

import com.elm.common.Result;
import com.elm.vo.MerchantVO;

import java.util.List;

public interface MerchantService {
    Result<List<MerchantVO>> list(Integer categoryId, String sort);
    Result<MerchantVO> detail(Integer merchantId);
}