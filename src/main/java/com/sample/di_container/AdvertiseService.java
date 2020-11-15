package com.sample.di_container;

public class AdvertiseService {

    final ProductRepository productRepository;
    final TwitterAdapter twitterAdapter;

    public AdvertiseService(final ProductRepository productRepository, final TwitterAdapter twitterAdapter) {
        this.productRepository = productRepository;
        this.twitterAdapter = twitterAdapter;
    }

    public void advertise(final int productId, final String promotionMessage) {
        // mainメソッドからproductRepositoryとtwitterAdapterを注入
        Product product = productRepository.findById(productId);
        String postMessage = promotionMessage + ", " + product.getProductName();
        Tweet tweet = new Tweet(postMessage);
        twitterAdapter.post(tweet);
    }
}
