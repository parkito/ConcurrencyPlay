package com.concurrency.play.language.java.advance.completable.tutorial;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Crating {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //synchronous
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete("Future's Result");
        String result = completableFuture.get();
        System.out.println(result);


        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Simulate a long-running Job
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("DONE");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("I'll run in a separate thread than the main thread.");
        });
        System.out.println("Main threadgit ");
    }
}
