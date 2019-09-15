package com.concurrency.play.language.java.advance.example;

import com.concurrency.play.utils.Utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimpleFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //chain for separate thread
        CompletableFuture<Work> work1 = CompletableFuture.completedFuture(new Work());
        CompletableFuture<Work> work2 = work1.thenApplyAsync(Work::returnWork);
        CompletableFuture<Work> work3 = work2.thenApplyAsync(Work::returnWork);
        CompletableFuture<Work> work4 = work3.thenApplyAsync(Work::returnWork);
        work4.get().doWork();

        //Task in severL threads
        Future<String> completableFuture = calculateAsync();
        System.out.println(completableFuture.get());

        //Async running
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            Utils.sleepSeconds(2);
        });
        CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName()));

        System.out.println(CompletableFuture.supplyAsync(() -> 99).get());
    }

    public static Future<String> calculateAsync() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        service.shutdown();

        return completableFuture;
    }
}
