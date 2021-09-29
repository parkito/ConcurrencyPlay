package com.siksmfp.concurrency.common;

import java.util.concurrent.TimeUnit;

public class Threads {

    public static void sleepSeconds(int duration) {
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
