package com.concurrency.play.language.java.advance.completable.tutorial;

import com.concurrency.play.Utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Creating {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //synchronous
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete("Future's Result");
        String result = completableFuture.get();
        System.out.println(result);

        //async execution
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            Utils.sleepSeconds(1);
            System.out.println("Run " + Thread.currentThread().getName());
        });

        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            Utils.sleepSeconds(1);
            System.out.println("Supply " + Thread.currentThread().getName());
            return "Result of the asynchronous computation";
        });

        System.out.println("Main thread: " + Thread.currentThread().getName());
        System.out.println("Main thread is working");
        System.out.println("Async result " + runAsync.get() + " " + supplyAsync.get());

        //use own thread pool
        Executor executor = Executors.newFixedThreadPool(10);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Utils.sleepSeconds(1);
            return "Result of the asynchronous computation";
        }, executor);
    }
}
