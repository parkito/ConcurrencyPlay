package com.concurrency.play.completable.advance.example;

import com.concurrency.play.utils.Utils;

import java.util.Random;

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
        Utils.sleepSeconds(RANDOM.nextInt(max - min + 1) + min);
        System.out.println("Modifying " + Thread.currentThread().getName() + " finished");
        return this;
    }
}
