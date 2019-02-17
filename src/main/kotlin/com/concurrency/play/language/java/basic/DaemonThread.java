package com.concurrency.play.language.java.basic;

import java.util.concurrent.TimeUnit;

public class DaemonThread implements Runnable {
    private int i;
    public DaemonThread(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("Starting " + threadName);

        try {
            TimeUnit.SECONDS.sleep(i);
//            throw new InterruptedException("Interrupting");
        } catch (InterruptedException e) {
            System.out.println("Catch " + threadName);
            e.printStackTrace();
        } finally {
            System.out.println("Finally " + threadName);
        }
        System.out.println("Finished " + threadName);
    }
}
