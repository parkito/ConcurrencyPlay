package com.siksmfp.concurrency.algo.completable.basic.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class CompletionService {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        java.util.concurrent.CompletionService<String> ecs = new ExecutorCompletionService<>(executor);

        List<Future<String>> futureList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int finalI = 5 - i;
            futureList.add(ecs.submit(() -> {
                sleepSeconds(finalI);
                return String.valueOf(finalI);
            }));
        }

        for (int i = 0; i < 3; i++) {
            try {
                System.out.println(ecs.take().get()); //waiting for adding task to queue if queue is empty
            } catch (Exception e) {
                futureList.forEach(f -> f.cancel(true));
            }
        }

        executor.shutdown();
    }
}
