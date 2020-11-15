package com.sample.di_container;

public class ProductRepositoryImpl {

    public Product findById(int productId) {
        return new Product(productId, "おすすめ商品");
    }
}
