package com.concurrency.play.algorithm;

public class ThreadID {

    public static int get() {
        String threadName = Thread.currentThread().getName();
        try {
            return Integer.parseInt(threadName);
        } catch (Exception ex) {
            throw new IllegalStateException("Can't parse number of thread " + threadName);
        }
    }
}
