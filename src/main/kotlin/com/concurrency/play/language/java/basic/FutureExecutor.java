package com.concurrency.play.language.java.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Future<String>> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(15);

        for (int i = 0; i < 10; i++) {
            list.add(service.submit(new Call(i)));
        }

        System.out.println("Future " + list.size());

        for (Future<String> stringFuture : list) {
            System.out.println(stringFuture.get());
        }

        service.shutdown();

    }
}
