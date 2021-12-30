package com.siksmfp.concurrency.algo.common;

import java.util.SplittableRandom;

public class Rnd {

    public static int random() {
        return new SplittableRandom().nextInt(0, Integer.MAX_VALUE);
    }

    public static int random(int from, int to) {
        return new SplittableRandom().nextInt(from, to);
    }

    public static int random_5() {
        return new SplittableRandom().nextInt(0, 5);
    }

    public static int random_10() {
        return new SplittableRandom().nextInt(0, 10);
    }

    public static int random_100() {
        return new SplittableRandom().nextInt(0, 100);
    }
}
