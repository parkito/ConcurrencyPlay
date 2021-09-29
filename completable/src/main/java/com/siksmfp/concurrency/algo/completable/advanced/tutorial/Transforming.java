package com.siksmfp.concurrency.algo.completable.advanced.tutorial;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class Transforming {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        thenApply();
        applySequence();
        thenAccept();
        thenRun();
    }

//To have more control over the thread that executes the callback task,
// you can use async callbacks.
// If you use thenApplyAsync() callback,
// then it will be executed in a different thread obtained from ForkJoinPool.commonPool()

    private static void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> whatsYourNameFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("First future");
            sleepSeconds(1);
            return "Artyom";
        });

        CompletableFuture<String> greetingFuture = whatsYourNameFuture.thenApply(name -> {
            System.out.println("Second future");
            sleepSeconds(1);
            return "Hello " + name;
        });

        sleepSeconds(2);
        System.out.println(greetingFuture.get());
    }

    private static void applySequence() throws ExecutionException, InterruptedException {
        CompletableFuture<String> welcomeText = CompletableFuture.supplyAsync(() -> {
                    sleepSeconds(2);
                    return "Artyom";
                })
                .thenApply(name -> "Hello " + name)
                .thenApply(greeting -> greeting + ", Welcome to the CalliCoder Blog");

        System.out.println(welcomeText.get());
    }

    private static void thenAccept() {

        class InnerObject {
            public String name;

            public InnerObject() {
                name = "default";
            }
        }
        CompletableFuture.supplyAsync(InnerObject::new)
                .thenAccept(io -> System.out.println(io.name));
    }

    private static void thenRun() {
        CompletableFuture.supplyAsync(() -> {
            sleepSeconds(1);
            return "String";
        }).thenRun(() -> {
            System.out.println("just doing something");
        });
    }
}
