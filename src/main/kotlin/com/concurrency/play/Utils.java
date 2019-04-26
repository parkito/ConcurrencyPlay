package com.concurrency.play;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static void sleepSeconds(int duration) {
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
