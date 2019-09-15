package com.concurrency.play.language.java.completable.basic.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FutureExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Future<String>> list = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(15);

        for (int i = 0; i < 10; i++) {
            list.add(service.submit(new Call(i)));
        }

        System.out.println("Future " + list.size());

        for (Future<String> stringFuture : list) {
            System.out.println(stringFuture.get());
        }

        service.shutdown();


        //new style

        ExecutorService service1 = Executors.newFixedThreadPool(10);

        List<CallableDistance> tasks = IntStream.range(100, 110)
                .mapToObj(i -> new CallableDistance(i, i + 2))
                .collect(Collectors.toList());

        List<Future<Integer>> futures = service1.invokeAll(tasks);

        futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList())
        .forEach(System.out::println);

        service1.shutdown();

    }
}
