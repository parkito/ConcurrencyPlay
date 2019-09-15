package com.concurrency.play.language.java.completable.basic.blocking;

public class WaitNotify {

    public static void main(String[] args) {
        Queue queue = new Queue("0");
        new Producer(queue).start();
        new Consumer(queue).start();
    }

    static class Queue {

        public String element;
        private boolean isBlocked = false;

        public Queue(String element) {
            this.element = element;
        }

        public synchronized String get() {
            while (!isBlocked) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Getting element " + element);
            isBlocked = false;
            notify();
            return element;
        }

        public synchronized void add(String s) {
            while (isBlocked) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            element = s;
            System.out.println("Adding element " + element);
            isBlocked = true;
            notify();
        }
    }

    static class Consumer extends Thread {
        private Queue queue;
        private Thread t;

        public Consumer(Queue queue) {
            this.queue = queue;
            t = new Thread(this);
        }

        @Override
        public void run() {
            int i = 0;
            while (i < 100) {
                i = Integer.parseInt(queue.get());
            }
        }
    }

    static class Producer extends Thread {
        private Queue queue;
        private Thread t;

        public Producer(Queue queue) {
            this.queue = queue;
            t = new Thread(this);

        }

        @Override
        public void run() {
            int i = 0;
            while (i < 100) {
                i++;
                queue.add(String.valueOf(i));
            }
        }
    }
}
