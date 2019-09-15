package com.concurrency.play.language.java.completable.basic.executor;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Call implements Callable<String> {
    private static final int min = 0, max = 1;
    private Random random = new Random();

    private int size;

    public Call(int size) {
        this.size = size;
    }

    @Override
    public String call() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(i);
            int sleepTime = random.nextInt(max - min + 1) + min;
            TimeUnit.SECONDS.sleep(sleepTime);
        }

        System.out.println(Thread.currentThread().getName() + " is finished");
        return builder.toString();
    }
}
