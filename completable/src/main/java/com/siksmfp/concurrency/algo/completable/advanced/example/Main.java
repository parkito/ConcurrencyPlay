package com.siksmfp.concurrency.algo.completable.advanced.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class Main {
    public static void main(String[] args) {
        System.out.println(a());
    }

    public static int a() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            sleepSeconds(2);
            System.out.println("exit");
        });
        System.out.println("Finish");
        return 1;
    }
}
