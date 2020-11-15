package com.sample.di_container;

public class AdvertiseService {

    public void advertise(final int productId, final String promotionMessage) {
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        Product product = productRepository.findById(productId);
        String postMessage = promotionMessage + "," + product.getProductName();
        Tweet tweet = new Tweet(postMessage);

        TwitterAdapterImpl twitterAdapter = new TwitterAdapterImpl();
        twitterAdapter.post(tweet);
    }
}
