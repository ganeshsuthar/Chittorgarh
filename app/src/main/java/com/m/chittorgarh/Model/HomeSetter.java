package com.m.chittorgarh.Model;

/**
 * Created by Admins on 5/11/2017.
 */

public class HomeSetter {
    String productImage,productName;

    public HomeSetter(String productImage, String productName) {
        this.productImage = productImage;
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
