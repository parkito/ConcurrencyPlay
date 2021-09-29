package com.siksmfp.concurrency.algo.completable.advanced.example;

import java.util.concurrent.CompletableFuture;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class AsyncCompletable {

    public static void main(String[] args) {
        Integer join = CompletableFuture
                .completedFuture(1)
                .thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    sleepSeconds(1);
                    return ++i;
                })
                .thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    sleepSeconds(2);
                    return ++i;
                })
                .thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    sleepSeconds(3);
                    return ++i;
                }).thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    return ++i;
                }).thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    return ++i;
                }).thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    return ++i;
                }).thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    return ++i;
                }).thenApplyAsync(i -> {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    return ++i;
                }).join();

        System.out.println(join);
    }
}
