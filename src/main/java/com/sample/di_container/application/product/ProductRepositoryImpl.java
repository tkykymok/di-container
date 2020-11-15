package com.sample.di_container.application.product;

import javax.inject.Named;

@Named
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Product findById(int productId) {
        return new Product(productId, "おすすめ商品");
    }

}
