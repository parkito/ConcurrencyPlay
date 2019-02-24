package com.concurrency.play.language.java.advance.completable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class Pipeline {
    public static void main(String[] args) {

        generateNamesTask.get().forEach(fileName ->
                CompletableFuture.completedFuture(fileName)
                        .thenApplyAsync(createFileTask)
                        .thenApply(addTimeTask)
                        .thenAccept(System.out::println)
        );


    }

    public static Supplier<List<String>> generateNamesTask = () -> {
        Random rand = new Random();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            result.add(UUID.randomUUID().toString());
        }
        return result;
    };

    public static Function<String, String> createFileTask = (String file) -> {

        try {
            Files.createFile(Paths.get(file + ".txt"));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
        return file;
    };

    public static Function<String, String> addTimeTask = (fileName) -> {
        List<String> timestamp = Arrays.asList(new Date().toString(), Thread.currentThread().getName());
        try {
            Files.write(Paths.get(fileName + ".txt"), timestamp);
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
        return fileName;
    };
}

