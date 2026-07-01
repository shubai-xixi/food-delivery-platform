package com.elm.controller;

import com.elm.common.Result;
import com.elm.common.UserContext;
import com.elm.dto.AddressDTO;
import com.elm.service.AddressService;
import com.elm.vo.AddressVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址模块", description = "收货地址增删改查")
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "新增地址")
    @PostMapping
    public Result<AddressVO> add(@RequestBody AddressDTO dto) {
        Long userId = UserContext.getUserId();
        return addressService.add(userId, dto);
    }

    @Operation(summary = "删除地址")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        return addressService.delete(userId, id);
    }

    @Operation(summary = "修改地址")
    @PutMapping("/{id}")
    public Result<AddressVO> update( @PathVariable Long id, @RequestBody AddressDTO dto) {
        Long userId = UserContext.getUserId();
        return addressService.update(userId, id, dto);
    }

    @Operation(summary = "查询用户所有地址")
    @GetMapping
    public Result<List<AddressVO>> list() {
        Long userId = UserContext.getUserId();
        return addressService.list(userId);
    }

    @Operation(summary = "设置默认地址")
    @PutMapping("/{id}/default")
    public Result<?> setDefault(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        return addressService.setDefault(userId, id);
    }

    @Operation(summary = "根据ID查询地址")
    @GetMapping("/{id}")
    public Result<AddressVO> getById(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        return addressService.getById(userId, id);
    }

}