package com.sample.di_container.application.tweet;

public class TwitterAdapterImpl implements TwitterAdapter{
    @Override
    public void post(Tweet tweet) {
        System.out.println(tweet);
    }
}