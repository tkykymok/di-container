package com.sample.di_container.support;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sample.di_container.application.AdvertiseService;
import com.sample.di_container.application.tweet.TwitterAdapter;

public class ContextTest {
    @Test
    @DisplayName("Beanの登録テスト")
    void test1() throws Exception {
        final Context context = new Context();
        // AdvertiseServiceを取得
        final AdvertiseService advertiseService = context.getBean(AdvertiseService.class);
        assertNotNull(advertiseService);

        // BeanクラスがInjectされていること
        Field field = advertiseService.getClass().getDeclaredField("twitterAdapter");
        field.setAccessible(true);
        final Object twitterAdapter = field.get(advertiseService);
        assertNotNull(twitterAdapter);
        assertTrue(TwitterAdapter.class.isAssignableFrom(twitterAdapter.getClass()));        }

    }