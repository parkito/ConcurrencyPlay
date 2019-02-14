package com.concurrency.play.language.java.basic;

import java.util.concurrent.ExecutorService;

public class Executors {
    public static void main(String[] args) {
        ExecutorService executor = java.util.concurrent.Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executor.execute(new Task());
        }
        executor.shutdown();
    }

}