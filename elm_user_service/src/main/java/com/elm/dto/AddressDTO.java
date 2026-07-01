package com.elm.dto;

import lombok.Data;

@Data
public class AddressDTO {
    /** 联系人姓名 */
    private String contactName;
    /** 联系人手机号 */
    private String contactPhone;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 区 */
    private String district;
    /** 详细地址 */
    private String detail;
    /** 标签 */
    private String label;
}