package com.elm.service;

import com.elm.common.Result;
import com.elm.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    Result<List<CategoryVO>> list();
}