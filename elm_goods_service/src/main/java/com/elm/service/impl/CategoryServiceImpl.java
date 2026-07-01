package com.elm.service.impl;

import com.elm.common.Result;
import com.elm.entity.Category;
import com.elm.mapper.CategoryMapper;
import com.elm.service.CategoryService;
import com.elm.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public Result<List<CategoryVO>> list() {
        List<Category> list = categoryMapper.selectList(null);
        List<CategoryVO> voList = list.stream().map(c -> CategoryVO.builder()
                .id(c.getId())
                .name(c.getName())
                .iconUrl(c.getIconUrl())
                .sortOrder(c.getSortOrder())
                .build()).collect(Collectors.toList());
        return Result.ok(voList);
    }
}