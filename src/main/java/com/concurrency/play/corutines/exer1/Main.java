package com.concurrency.play.corutines.exer1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int NUM = 10;
    private static final int MIN = 0, MAX = 9;

    public static ChopStick[] chopSticks = createChopSticks(NUM);
    public static Philosopher[] philosophers = createPhilosophers(NUM);

    private static Random rand = new Random();

    public static void main(String[] args) throws InterruptedException {
        while (true){
            int philosopherToEat = rand.nextInt(MAX - MIN + 1) + MIN;
            TimeUnit.SECONDS.sleep(1);
            new Thread(philosophers[philosopherToEat]).start();
        }
    }

    private static ChopStick[] createChopSticks(int n) {
        ChopStick[] chopSticks = new ChopStick[n];
        for (int i = 0; i < n; i++) {
            chopSticks[i] = new ChopStick(i);
        }
        return chopSticks;
    }

    private static Philosopher[] createPhilosophers(int n) {
        Philosopher[] philosophers = new Philosopher[n];
        for (int i = 0; i < n; i++) {
            philosophers[i] = new Philosopher(i);
        }
        return philosophers;
    }
}
