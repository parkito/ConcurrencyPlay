package com.concurrency.play.language.java.basic;

import java.util.Random;

public class Task implements Runnable {
    private Random random = new Random();
    private static final int min = 0, max = 100;

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " data " + i);
            try {
                Thread.sleep(random.nextInt(max - min + 1) + min);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
