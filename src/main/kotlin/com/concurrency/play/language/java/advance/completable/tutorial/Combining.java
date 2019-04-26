package com.concurrency.play.language.java.advance.completable.tutorial;

import com.concurrency.play.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Combining {

    private static class User {
    }

    private static CompletableFuture<User> getUsersDetail(String userId) {
        return CompletableFuture.supplyAsync(() -> UserService.getUserDetails(userId));
    }

    private static CompletableFuture<Double> getCreditRating(User user) {
        return CompletableFuture.supplyAsync(() -> CreditRatingService.getCreditRating(user));
    }

    private static class UserService {
        static User getUserDetails(String userId) {
            return new User();
        }
    }

    private static class CreditRatingService {
        static Double getCreditRating(User user) {
            return 0.0;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Combine two dependent futures using thenCompose()
        CompletableFuture<CompletableFuture<Double>> result = getUsersDetail("id")
                .thenApply(Combining::getCreditRating);

        CompletableFuture<Double> doubleResult = getUsersDetail("id")
                .thenCompose(Combining::getCreditRating);

        //Combine two independent futures using thenCombine()

        System.out.println("Retrieving weight.");
        CompletableFuture<Double> weightInKgFuture = CompletableFuture.supplyAsync(() -> {
            Utils.sleepSeconds(1);
            return 65.0;
        });

        System.out.println("Retrieving height.");
        CompletableFuture<Double> heightInCmFuture = CompletableFuture.supplyAsync(() -> {
            Utils.sleepSeconds(1);
            return 177.8;
        });

        System.out.println("Calculating BMI.");
        CompletableFuture<Double> combinedFuture = weightInKgFuture
                .thenCombine(heightInCmFuture, (weightInKg, heightInCm) -> {
                    Double heightInMeter = heightInCm / 100;
                    return weightInKg / (heightInMeter * heightInMeter);
                });

        System.out.println("Your BMI is - " + combinedFuture.get());


        //CompletableFuture.allOf is used in scenarios when you have a List of independent futures
        // that you want to run in parallel and do something after all of them are complete.

        List<String> webPageLinks = Arrays.asList("1", "2");    // A list of 100 web page links

        // Download contents of all the web pages asynchronously
        List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
                .map(Combining::downloadWebPage)
                .collect(Collectors.toList());

        // Create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(// Returns a new CompletableFuture that is completed when all of the given CompletableFutures complet
                pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()])
        );

        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> pageContentFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
        );

    }

    private static CompletableFuture<String> downloadWebPage(String pageLink) {
        return CompletableFuture.supplyAsync(() -> {
            // Code to download and return the web page's content
            return "";
        });
    }
}
