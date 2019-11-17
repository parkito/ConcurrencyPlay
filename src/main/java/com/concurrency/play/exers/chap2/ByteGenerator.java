package com.concurrency.play.exers.chap2;

import com.concurrency.play.utils.Utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteGenerator {
    private static final int INITIAL_VALUE = Byte.MIN_VALUE - 1;

    private final AtomicInteger counter = new AtomicInteger(INITIAL_VALUE);
    private final AtomicInteger resetCounter = new AtomicInteger(0);
    private final Object lock = new Object();

    public byte nextValue() {
        int next = counter.incrementAndGet();
        System.out.println(next);
        if (next > Byte.MAX_VALUE) {
            synchronized (lock) {
                int i = counter.get();
                if (i > Byte.MAX_VALUE) {
                    counter.set(INITIAL_VALUE);
                    resetCounter.incrementAndGet();
                }
                next = counter.incrementAndGet();
            }
        }
        return (byte) next;
    }

    public byte correctNext() {
        return (byte)counter.incrementAndGet();
    }

    public static void main(String[] args) {
        ByteGenerator bg = new ByteGenerator();
        Queue<Byte> queue = new ConcurrentLinkedQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    byte next = bg.nextValue();
                    queue.add(next);
                }
            });
        }
        executor.shutdown();
        Utils.sleepSeconds(5);

        System.out.println(queue.size());
    }
}
