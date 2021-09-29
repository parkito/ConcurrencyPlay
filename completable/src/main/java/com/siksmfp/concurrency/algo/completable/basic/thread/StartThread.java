package com.siksmfp.concurrency.algo.completable.basic.thread;

public class StartThread {

    public static void main(String[] args) {
        Thread[] threadArr1 = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threadArr1[i] = new Thread(new CustomRun());
            threadArr1[i].start();
        }

        System.out.println("1---------------");

        for (int i = 0; i < 10; i++) {
            new CustomThread().start();
        }

        System.out.println("2---------------");
    }

    static class CustomRun implements Runnable {
        private String[] toPrint = {"1", "2", "3", "4", "5"};

        @Override
        public void run() {
            for (String s : toPrint) {
                System.out.println(Thread.currentThread().getName() + " " + s);
            }
        }
    }

    static class CustomThread extends Thread {
        private Thread currentThread;
        private String[] toPrint = {"1", "2", "3", "4", "5"};

        public CustomThread() {
            currentThread = new Thread(this);
        }

        @Override
        public void run() {
            for (String s : toPrint) {
                System.out.println(Thread.currentThread().getName() + " " + s);
            }
        }
    }
}

