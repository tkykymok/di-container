package com.sample.di_container;

import com.sample.di_container.application.AdvertiseService;
import com.sample.di_container.application.product.ProductRepository;
import com.sample.di_container.application.product.ProductRepositoryImpl;
import com.sample.di_container.application.tweet.TwitterAdapter;
import com.sample.di_container.application.tweet.TwitterAdapterImpl;

/**
 * Hello world!
 *
 */
public class Application {
    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepositoryImpl();
        TwitterAdapter twitterAdapter = new TwitterAdapterImpl();

        AdvertiseService advertiseService = new AdvertiseService(productRepository, twitterAdapter);
        advertiseService.advertise(1, "販促メッセージ");
    }
}
