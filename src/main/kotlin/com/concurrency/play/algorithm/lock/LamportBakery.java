package com.concurrency.play.algorithm.lock;

import com.concurrency.play.algorithm.lock.api.Lock;
import com.concurrency.play.utils.ThreadID;

import java.util.ArrayList;
import java.util.List;

//Lamport’s Bakery locking algorithm works for n threads
public class LamportBakery implements Lock {
    private int theadNumber;
    private List<Integer> threads = new ArrayList<>(theadNumber); // ticket for threads in line, n - number of threads
    private List<Boolean> flags = new ArrayList<>(theadNumber); // True when thread flags in line

    public LamportBakery(int threadNumber) {
        this.theadNumber = threadNumber;
        for (int i = 0; i < threadNumber; i++) {
            threads.add(0);
            flags.add(false);
        }
    }

    @Override
    public void lock() {
        int threadId = ThreadID.get();

        flags.set(threadId, true);
        int max = 0;
        for (int ticket : threads) {
            max = Math.max(max, ticket);
        }

        threads.set(threadId, 1 + max);
        flags.set(threadId, false);

        for (int i = 0; i < threads.size(); ++i) {
            if (i != threadId) {
                while (flags.get(i)) {
                    Thread.yield();
                } // wait while other thread picks a flag

                while (threads.get(i) != 0
                        && (threads.get(threadId) > threads.get(i)
                        || (threads.get(threadId) == threads.get(i) && threadId > i))
                ) {
                    Thread.yield();
                }
            }
        }
    }

    @Override
    public void unlock() {
        threads.set(ThreadID.get(), 0);
    }
}
