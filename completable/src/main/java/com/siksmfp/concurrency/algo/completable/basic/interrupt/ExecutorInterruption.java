package com.siksmfp.concurrency.algo.completable.basic.interrupt;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorInterruption {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<String> f = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(4);
            return "Result";
        });

        f.cancel(false);

        try {
            f.get();
        } catch (InterruptedException e) {
            System.out.println("Interruption exception");
        } catch (ExecutionException e) {
            System.out.println("Execution exception");
        }finally {
            executorService.shutdown();
        }

    }
}
