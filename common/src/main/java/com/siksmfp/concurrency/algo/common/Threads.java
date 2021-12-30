package com.siksmfp.concurrency.algo.common;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Threads {

    public static void sleepSeconds(int duration) {
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static String threadId() {
        return Thread.currentThread().getName();
    }

    public static void printThread(String... msgs) {
        System.out.println("Thread " + threadId() + " is doing " + Arrays.toString(msgs));
    }
}
