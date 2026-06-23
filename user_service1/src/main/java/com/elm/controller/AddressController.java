// AddressController.java
package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.entity.Address;
import com.elm.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // 新增地址
    @PostMapping
    public Result<Address> add(@RequestHeader("X-User-Id") Long userId,
                               @RequestBody AddressDTO dto) {
        return Result.ok(addressService.add(userId, dto));
    }

    // 查询所有地址
    @GetMapping("/list")
    public Result<List<Address>> list(@RequestHeader("X-User-Id") Long userId) {
        return Result.ok(addressService.list(userId));
    }

    // 编辑地址
    @PutMapping("/{id}")
    public Result<Address> update(@PathVariable Long id,
                                  @RequestBody AddressDTO dto) {
        return Result.ok(addressService.update(id, dto));
    }

    // 删除地址
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        addressService.delete(id);
        return Result.ok();
    }

    // 设为默认地址
    @PutMapping("/{id}/default")
    public Result<?> setDefault(@RequestHeader("X-User-Id") Long userId,
                                @PathVariable Long id) {
        addressService.setDefault(userId, id);
        return Result.ok();
    }
}