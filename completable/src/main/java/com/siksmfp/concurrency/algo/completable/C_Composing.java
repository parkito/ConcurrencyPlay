package com.siksmfp.concurrency.algo.completable;

import com.siksmfp.concurrency.algo.common.Threads;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class C_Composing {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //combining type related futures
        findUserIdByEmail("email")
                .thenApply(C_Composing::findGroupById) //should work with completable future
                .thenAccept(
                        cf -> System.out.println(cf.join())
                ).join();

        findUserIdByEmail("email")
                .thenComposeAsync(C_Composing::findGroupById)//extract the result form the completable future
                .thenAccept(System.out::println)
                .join();

        //combine not related futures

        System.out.println("Retrieving weight.");
        CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> 65.0);

        System.out.println("Retrieving height.");
        CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> 177.8);

        CompletableFuture<Double> combinedFuture = weightInKgFuture
                .thenCombine(heightInCmFuture, (weightInKg, heightInCm) -> {
                    Double heightInMeter = heightInCm / 100;
                    return weightInKg / (heightInMeter * heightInMeter);
                });

        System.out.println("Your BMI is - " + combinedFuture.get());

        //combining more futures
        List<String> webPageLinks = Arrays.asList("1.html", "2.html");

        var futures = webPageLinks.stream()
                .map(CompletableFuture::completedFuture);

        CompletableFuture<Void> completedFutures = CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new)
        );

        List<String> results = completedFutures.thenApply(
                f -> futures.map(CompletableFuture::join) //futures already completed
                        .collect(Collectors.toList())
        ).get();

    }

    private static CompletableFuture<String> findUserIdByEmail(String email) {
        System.out.println(Threads.threadId());
        return CompletableFuture.completedFuture(email + "_id");
    }

    private static CompletableFuture<Long> findGroupById(String id) {
        System.out.println(Threads.threadId());
        return CompletableFuture.completedFuture(new Random().nextLong() + id.length());
    }
}
