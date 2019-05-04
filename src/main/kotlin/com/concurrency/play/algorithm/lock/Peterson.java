package com.concurrency.play.algorithm.lock;

import com.concurrency.play.algorithm.lock.api.Lock;
import com.concurrency.play.utils.ThreadID;

//Peterson locking algorithm. Works only for 2 threads
public class Peterson implements Lock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    @Override
    public void lock() {
        int i = ThreadID.get();
        int j = 1 - i;
        flag[j] = true;
        victim = i;
        // wait
        while (flag[j] && victim == i) {
//            System.out.println("Thread " + ThreadID.get() + " is waiting");
        }
    }

    @Override
    public void unlock() {
        int i = ThreadID.get();
        flag[i] = false;
    }
}