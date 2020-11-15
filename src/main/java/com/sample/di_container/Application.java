package com.sample.di_container;

/**
 * Hello world!
 *
 */
public class Application {
    public static void main(String[] args) {
        AdvertiseService advertiseService = new AdvertiseService ();
        advertiseService.advertise(1, "販促メッセージ");
    }
}
