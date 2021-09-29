package com.siksmfp.concurrency.algo.completable.basic.thread;

import java.util.concurrent.*;

public class ExceptionHandling {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool(new MyHandlerThreadFactory());
        exec.execute(() -> {
            throw new RuntimeException("Ex message 1");
        });

        Future<Object> submit = exec.submit(() -> {
            throw new RuntimeException("Ex message 2");
        });

        try {
            submit.get();
        } catch (Exception e) {
            System.out.println("Try caught " + e.getMessage());
        }
        exec.shutdown();
    }
}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Caught " + e);
    }
}

class MyHandlerThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        System.out.println("Thread factory is working");
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        return t;
    }
}