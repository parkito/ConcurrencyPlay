package com.siksmfp.concurrency.algo.algo;

import com.siksmfp.concurrency.algo.common.ThreadID;
import com.siksmfp.concurrency.algo.algo.lock.LamportBakery;
import com.siksmfp.concurrency.algo.algo.lock.Peterson;
import com.siksmfp.concurrency.algo.algo.lock.api.Lock;
import com.siksmfp.concurrency.algo.common.Threads;

public class LockingExample {
    private static final String ZERO_THREAD = "0";
    private static final String FIRST_THREAD = "1";
    private static final String SECOND_THREAD = "2";
    private static final String THIRD_THREAD = "3";
    private static final String FOURTH_THREAD = "4";
    private static final String FIFTH_THREAD = "5";

    public static void main(String[] args) {
        petersonExample();
        lamportExample();
    }

    private static void petersonExample() {
        Lock peterson = new Peterson();
        new Thread(() -> threadWork(peterson), ZERO_THREAD).start();
        new Thread(() -> threadWork(peterson), FIRST_THREAD).start();

        System.out.println("Main thread is finished");
    }

    private static void lamportExample() {
        Lock lamportBakery = new LamportBakery(6);
        new Thread(() -> threadWork(lamportBakery), ZERO_THREAD).start();
        new Thread(() -> threadWork(lamportBakery), FIRST_THREAD).start();
        new Thread(() -> threadWork(lamportBakery), SECOND_THREAD).start();
        new Thread(() -> threadWork(lamportBakery), THIRD_THREAD).start();
        new Thread(() -> threadWork(lamportBakery), FOURTH_THREAD).start();
        new Thread(() -> threadWork(lamportBakery), FIFTH_THREAD).start();

        System.out.println("Main thread is finished");
    }

    private static void threadWork(Lock peterson) {
        try {
            System.out.println("Thread " + ThreadID.get() + " try acquire lock");
            peterson.lock();
            System.out.println("Thread " + ThreadID.get() + " acquired lock");
            Threads.sleepSeconds(1);
            System.out.println("Thread " + ThreadID.get() + " is working");
        } finally {
            peterson.unlock();
            System.out.println("Thread " + ThreadID.get() + " released lock");
        }
    }
}
