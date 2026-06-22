package com.elm.controller;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.dto.RegisterDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rest")
public class RestTemplateController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/user/{userId}")
    public Result<?> getUser(@PathVariable Long userId) {
        System.out.println("[RestTemplate] GET userId=" + userId);
        String url = "http://localhost:18081/user/info/" + userId;
        return restTemplate.getForObject(url, Result.class);
    }

    @PostMapping("/user")
    public Result<?> addUser(@RequestBody RegisterDTO dto) {
        System.out.println("[RestTemplate] POST body=" + dto);
        String url = "http://localhost:18081/user/register";
        return restTemplate.postForObject(url, dto, Result.class);
    }

    @PutMapping("/address/{id}")
    public Result<?> updateAddress(@PathVariable Long id, @RequestBody AddressDTO dto) {
        System.out.println("[RestTemplate] PUT id=" + id + " body=" + dto);
        String url = "http://localhost:18081/address/" + id;
        restTemplate.put(url, dto);
        return Result.ok();
    }

    @DeleteMapping("/address/{id}")
    public Result<?> deleteAddress(@PathVariable Long id) {
        System.out.println("[RestTemplate] DELETE id=" + id);
        String url = "http://localhost:18081/address/" + id;
        restTemplate.delete(url);
        return Result.ok();
    }
}