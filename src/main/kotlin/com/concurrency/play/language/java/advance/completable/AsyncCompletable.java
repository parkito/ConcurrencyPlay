package com.concurrency.play.language.java.advance.completable;

import java.util.concurrent.CompletableFuture;

public class AsyncCompletable {
    public static void main(String[] args) {
        Integer join = CompletableFuture
                .completedFuture(1)
                .thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                })
                .thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                })
                .thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                }).thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                }).thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                }).thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                }).thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                }).thenApplyAsync(i -> {
                    System.out.println(Thread.currentThread().getName());
                    return i;
                }).join();

        System.out.println(join);
    }
}
