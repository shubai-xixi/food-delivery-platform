// CartDTO.java
package com.elm.dto;

public class CartDTO {
    private Integer shopId;
    private Integer goodsId;
    private String goodsName;
    private String goodsImage;
    private String selectedSize;
    private String selectedTemperature;
    private String selectedSweetness;
    private Float price;
    private Integer quantity;

    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public Integer getGoodsId() { return goodsId; }
    public void setGoodsId(Integer goodsId) { this.goodsId = goodsId; }
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }
    public String getGoodsImage() { return goodsImage; }
    public void setGoodsImage(String goodsImage) { this.goodsImage = goodsImage; }
    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String selectedSize) { this.selectedSize = selectedSize; }
    public String getSelectedTemperature() { return selectedTemperature; }
    public void setSelectedTemperature(String selectedTemperature) { this.selectedTemperature = selectedTemperature; }
    public String getSelectedSweetness() { return selectedSweetness; }
    public void setSelectedSweetness(String selectedSweetness) { this.selectedSweetness = selectedSweetness; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}