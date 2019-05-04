package com.concurrency.play.algorithm.lock;

import com.concurrency.play.algorithm.ThreadID;
import com.concurrency.play.utils.Utils;

public class Peterson {
    private static final String ZERO_THREAD = "0";
    private static final String FIRST_THREAD = "1";

    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public void lock() {
        int i = ThreadID.get();
        int j = 1 - i;
        flag[j] = true;
        victim = i;
        // wait
        while (flag[j] && victim == i) {
//            System.out.println("Thread " + ThreadID.get() + " is waiting");
        }
    }

    public void unlock() {
        int i = ThreadID.get();
        flag[i] = false;
    }

    public static void main(String[] args) throws InterruptedException {
        Peterson peterson = new Peterson();
        new Thread(() -> threadWork(peterson), ZERO_THREAD).start();
        new Thread(() -> threadWork(peterson), FIRST_THREAD).start();

        System.out.println("Main thread is finished");
    }

    private static void threadWork(Peterson peterson) {
        try {
            System.out.println("Thread " + ThreadID.get() + " try acquire lock");
            peterson.lock();
            System.out.println("Thread " + ThreadID.get() + " acquired lock");
            Utils.sleepSeconds(3);
            System.out.println("Thread " + ThreadID.get() + " is working");
        } finally {
            peterson.unlock();
            System.out.println("Thread " + ThreadID.get() + " released lock");
        }
    }
}