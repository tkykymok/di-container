package com.sample.di_container;

public class Tweet {

    final String message;

    public Tweet(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "message：" + message;
    }
}
