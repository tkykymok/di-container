package com.sample.di_container.application.product;

public class Product {
    private final int productId;
    private final String productName;

    public Product(final int productId, final String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public String getProductName() {
        return "商品名：" + productName;
    }
}
