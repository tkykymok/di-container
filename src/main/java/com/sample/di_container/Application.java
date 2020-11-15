package com.sample.di_container;

import com.sample.di_container.application.AdvertiseService;
import com.sample.di_container.support.Context;

/**
 * Hello world!
 *
 */
public class Application {
    public static void main(String[] args) {
        Context context = new Context();
        final AdvertiseService advertiseService = context.getBean(AdvertiseService.class);
        advertiseService.advertise(1, "販促メッセージ");
    }
}
