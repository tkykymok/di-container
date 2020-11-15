package com.sample.di_container;

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
