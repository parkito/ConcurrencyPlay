package com.concurrency.play.language.java.advance.completable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Work {

    private static final Random RANDOM = new Random();
    private static final int min = 0, max = 5;
    private int internalCounter;

    public void doWork() {
        System.out.println("Internal counter " + internalCounter);
    }

    public Work returnWork() {
        System.out.println("Modifying "+Thread.currentThread().getName() +" starting");
        try {
            internalCounter++;
            TimeUnit.SECONDS.sleep(RANDOM.nextInt(max - min + 1) + min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Modifying "+ Thread.currentThread().getName() +" finished");
        return this;
    }
}
