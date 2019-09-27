package com.concurrency.play.completable.basic.daemon;

public class Daemons {
    public static void main(String[] args) {
        Thread t1 = new Thread(new DaemonThread(5));
        t1.start();

        Thread t2 = new Thread(new DaemonThread(5));
        t2.setDaemon(true);
        t2.start();

        System.out.println("Main thread is finished");
    }
}
