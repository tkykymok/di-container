package com.sample.di_container;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sample.di_container.application.AdvertiseService;
import com.sample.di_container.application.tweet.TwitterAdapter;
import com.sample.di_container.application.tweet.TwitterAdapterImpl;

public class ApplicationTest {
    @Nested
    class Context {
        @Test
        @DisplayName("context.getAdvertiseServiceBean()")
        void test1() {
            final Application.Context context = new Application.Context();

            final AdvertiseService advertiseService = context.getAdvertiseServiceBean();
            assertNotNull(advertiseService);
        }

        @Test
        @DisplayName("beanの登録")
        void testBeanRegister() {

            final Application.Context context = new Application.Context();
            // TODO 検証対象のクラスがプロダクションの都合に依存しているのをやめたい
            final TwitterAdapter beanClazz = context.getBean(TwitterAdapterImpl.class);
            assertNotNull(beanClazz);
        }
    }
}
