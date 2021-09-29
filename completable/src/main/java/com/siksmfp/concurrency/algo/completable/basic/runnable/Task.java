package com.siksmfp.concurrency.algo.completable.basic.runnable;

import java.util.Date;
import java.util.Random;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Task implements Runnable {
    private Random random = new Random();
    private static final int min = 0, max = 10;

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int sleepTime = random.nextInt(max - min + 1) + min;
            System.out.println(Thread.currentThread().getName() + " sleepTime " + sleepTime + " data + " + i + " date " + new Date());
            try {
                SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
