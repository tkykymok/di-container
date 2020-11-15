package com.sample.di_container;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        Context.register("productRepository", ProductRepositoryImpl.class);
        Context.register("twitterAdapter", TwitterAdapterImpl.class);

        AdvertiseService advertiseService = new AdvertiseService(
                (ProductRepository) Context.getBean("productRepository"),
                (TwitterAdapter) Context.getBean("twitterAdapter"));
        advertiseService.advertise(1, "販促メッセージ");
    }

    static class Context {
        static Map<String, Class> types = new HashMap<>();
        static Map<String, Object> beans = new HashMap<>();

        static void register(String name, Class type) {
            types.put(name, type);
        }

        static Object getBean(String name) {
            return beans.computeIfAbsent(name, key -> {
                Class type = types.get(name);
                Objects.requireNonNull(type, name + " not found.");
                try {
                    return type.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new RuntimeException(name + " can not instanciate", ex);
                }
            });
        }
    }
}
