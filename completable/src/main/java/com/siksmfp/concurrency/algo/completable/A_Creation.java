package com.siksmfp.concurrency.algo.completable;

import com.siksmfp.concurrency.algo.common.Threads;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class A_Creation {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var cp1 = new CompletableFuture<String>();
//        System.out.println(cp1.get()); //never completes, because nothing to complete
        cp1.complete("cp1");
        System.out.println(cp1.get());

        //async
        //return nothing
        var cp2 = CompletableFuture.runAsync(() -> {
            sleepSeconds(1);
            System.out.println(Threads.threadId());
        });
        //return a result
        var cp3 = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(1);
            System.out.println(Threads.threadId());
            return "result";
        });

        var executorService = Executors.newSingleThreadExecutor();
        var cp4 = CompletableFuture.supplyAsync(() -> {
            sleepSeconds(1);
            System.out.println(Threads.threadId());
            return "result";
        }, executorService);


        System.out.println("Point 1");
        cp2.get();
        System.out.println(cp3.get());
        executorService.shutdown();
    }
}
