package com.siksmfp.concurrency.algo.completable.advanced.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class Pipeline {
    public static void main(String[] args) {

        List<String> result = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        generateNamesTask.get().forEach(fileName -> {
                    try {
                        result.add(
                                CompletableFuture.completedFuture(fileName)
                                        .thenApplyAsync(createFileTask, executorService)
                                        .thenApplyAsync(addTimeTask, executorService)
                                        .get()
                        );
                    } catch (Exception ignored) {
                    }
                }
        );

        executorService.shutdown();
        System.out.println(result);
    }

    private static Supplier<List<String>> generateNamesTask = () -> {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            result.add(UUID.randomUUID().toString());
        }
        return result;
    };

    private static Function<String, String> createFileTask = (String file) -> {
        try {
            Files.createFile(Paths.get(file + ".txt"));
            sleepSeconds(2);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return file;
    };

    private static Function<String, String> addTimeTask = (fileName) -> {
        List<String> timestamp = Arrays.asList(new Date().toString(), Thread.currentThread().getName());
        try {
            Files.write(Paths.get(fileName + ".txt"), timestamp);
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
        return fileName;
    };
}

