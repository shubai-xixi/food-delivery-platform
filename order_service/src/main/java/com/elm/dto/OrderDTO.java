// OrderDTO.java
package com.elm.dto;

public class OrderDTO {
    private Integer shopId;
    private String shopName;
    private Long addressId;
    private String addressSnapshot;
    private Float totalPrice;
    private Float totalDiscountPrice;
    private Float deliveryFee;
    private Float actualPay;
    private String items;  // JSON字符串

    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
    public String getAddressSnapshot() { return addressSnapshot; }
    public void setAddressSnapshot(String addressSnapshot) { this.addressSnapshot = addressSnapshot; }
    public Float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Float totalPrice) { this.totalPrice = totalPrice; }
    public Float getTotalDiscountPrice() { return totalDiscountPrice; }
    public void setTotalDiscountPrice(Float totalDiscountPrice) { this.totalDiscountPrice = totalDiscountPrice; }
    public Float getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(Float deliveryFee) { this.deliveryFee = deliveryFee; }
    public Float getActualPay() { return actualPay; }
    public void setActualPay(Float actualPay) { this.actualPay = actualPay; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
}