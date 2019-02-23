package com.concurrency.play.language.java.basic.executor;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class CallableDistance implements Callable<Integer> {

    private static final Random RANDOM = new Random();
    private static final int min = 0, max = 10;

    private int speed;
    private int time;

    public CallableDistance(int speed, int time) {
        this.speed = speed;
        this.time = time;
    }

    @Override
    public Integer call() throws Exception {
        TimeUnit.SECONDS.sleep(RANDOM.nextInt(max - min + 1) + min);
        return speed * time;
    }

    @Override
    public String toString() {
        return "CallableDistance{" +
                "speed=" + speed +
                ", time=" + time +
                '}';
    }
}
