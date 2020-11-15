package com.sample.di_container.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sample.di_container.application.product.Product;
import com.sample.di_container.application.product.ProductRepository;
import com.sample.di_container.application.tweet.Tweet;
import com.sample.di_container.application.tweet.TwitterAdapter;

/**
 * Unit test for simple App.
 */
public class AdvertiseServiceTest {

    @Test
    @DisplayName("ツイートテスト")
    void advertise() {
        final ProductRepository productRepository = new ProductRepository() {
            @Override
            public Product findById(int productId) {
                // TODO 自動生成されたメソッド・スタブ
                return new Product(productId, "テスト商品");
            }
        };

        final TwitterAdapter duumyTweet = new TwitterAdapter() {
            @Override
            public void post(Tweet tweet) {
                System.out.println(tweet.toString());
                assertEquals("message：テストメッセージ, 商品名：テスト商品", tweet.toString());
            }
        };

        AdvertiseService advertiseService = new AdvertiseService(productRepository, duumyTweet);
        advertiseService.advertise(1, "テストメッセージ");

    }

}
