package com.concurrency.play.language.java.completable.advance.example;

import com.concurrency.play.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println(a());
    }

    public static int a() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            Utils.sleepSeconds(2);
            System.out.println("exit");
        });
        System.out.println("Finish");
        return 1;
    }
}
