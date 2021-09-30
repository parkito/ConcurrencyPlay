package com.siksmfp.concurrency.algo.completable;

import com.siksmfp.concurrency.algo.common.Threads;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class B_Chaining {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //transformation
        var ints = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println(Threads.threadId());
                    return IntStream.range(1, 10)
                            .boxed()
                            .collect(toList());
                }
        ).thenApply( //the same thread from parental expression
                list -> {
                    System.out.println(Threads.threadId());
                    return list.stream().map(i -> i + 1)
                            .collect(toList());
                }
        ).thenApplyAsync(//new thread
                list -> {
                    System.out.println(Threads.threadId());
                    return list.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(" "));
                }
        );

        System.out.println(ints.get());

        //consumers
        ints.thenAccept(nums -> {
            System.out.println(Threads.threadId());
            System.out.println(nums);
        });

        ints.thenAcceptAsync(nums -> {
            System.out.println(Threads.threadId());
            System.out.println(nums);
        });

        ints.thenRun(
                //no access to previous stage
                () -> System.out.println(Threads.threadId())
        );
    }
}
