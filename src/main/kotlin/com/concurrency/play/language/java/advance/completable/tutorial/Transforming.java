package com.concurrency.play.language.java.advance.completable.tutorial;

import com.concurrency.play.Utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Transforming {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        thenApply();
        applySequence();
    }

    private static void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> whatsYourNameFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("First future");
            Utils.sleepSeconds(1);
            return "Artyom";
        });

        CompletableFuture<String> greetingFuture = whatsYourNameFuture.thenApply(name -> {
            System.out.println("Second future");
            Utils.sleepSeconds(1);
            return "Hello " + name;
        });

        System.out.println(greetingFuture.get());
    }

    private static void applySequence() throws ExecutionException, InterruptedException {
        CompletableFuture<String> welcomeText = CompletableFuture.supplyAsync(() -> {
            Utils.sleepSeconds(2);
            return "Artyom";
        })
                .thenApply(name -> "Hello " + name)
                .thenApply(greeting -> greeting + ", Welcome to the CalliCoder Blog");

        System.out.println(welcomeText.get());
    }
}
