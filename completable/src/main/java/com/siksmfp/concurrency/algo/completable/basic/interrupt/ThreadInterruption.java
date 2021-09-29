package com.siksmfp.concurrency.algo.completable.basic.interrupt;

import java.util.concurrent.TimeUnit;

public class ThreadInterruption {
    class T1 implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                System.out.println("Interruption exception");
            }
        }
    }

    class T2 implements Runnable {
        @Override
        public void run() {
            System.out.println("Doing");
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted");
            }
        }
    }

    public static void main(String[] args) {
        ThreadInterruption threadInterruption = new ThreadInterruption();
        threadInterruption.funk();
    }

    public void funk() {
        Thread t1 = new Thread(new T1());
        t1.start();
        t1.interrupt();

        Thread t2 = new Thread(new T2());
        t2.start();
        t2.interrupt();

    }
}
