package com.elm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO {
    private Long id;
    private String contactName;
    private String contactPhone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String label;
    private Integer isDefault;
}