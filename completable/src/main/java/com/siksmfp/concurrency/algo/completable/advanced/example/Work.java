package com.siksmfp.concurrency.algo.completable.advanced.example;

import java.util.Random;

import static com.siksmfp.concurrency.algo.common.Threads.sleepSeconds;

public class Work {

    private static final Random RANDOM = new Random();
    private static final int min = 0, max = 5;
    private int internalCounter;

    public void doWork() {
        System.out.println("Internal counter " + internalCounter);
    }

    public Work returnWork() {
        System.out.println("Modifying " + Thread.currentThread().getName() + " starting");
        internalCounter++;
        sleepSeconds(RANDOM.nextInt(max - min + 1) + min);
        System.out.println("Modifying " + Thread.currentThread().getName() + " finished");
        return this;
    }
}
