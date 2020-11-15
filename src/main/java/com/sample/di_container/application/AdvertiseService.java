package com.sample.di_container.application;

import javax.inject.Inject;
import javax.inject.Named;

import com.sample.di_container.application.product.Product;
import com.sample.di_container.application.product.ProductRepository;
import com.sample.di_container.application.tweet.Tweet;
import com.sample.di_container.application.tweet.TwitterAdapter;

@Named
public class AdvertiseService {

    final ProductRepository productRepository;
    final TwitterAdapter twitterAdapter;

    @Inject
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
