package com.concurrency.play.corutines.exer1;

import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {

    private int name;

    public Philosopher(int name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Philosopher " + name + " starting eating");
        ChopStick left = null;
        ChopStick right;
        int leftStickNum = name;
        int rightStickNum = name == Main.chopSticks.length-1 ? 0 : name + 1;

        while (true) {

            if (Main.chopSticks[leftStickNum] != null) {
                left = Main.chopSticks[leftStickNum];
                Main.chopSticks[leftStickNum] = null;
            } else if (left != null) {
                System.out.println(name + " has left chopstick");
            } else {
                System.out.println("Philosopher " + name + " is waiting for left chopstick");
                sleep();
                continue;
            }

            if (Main.chopSticks[rightStickNum] != null) {
                right = Main.chopSticks[rightStickNum];
                Main.chopSticks[rightStickNum] = null;
            } else {
                System.out.println("Philosopher " + name + " is waiting for right chopstick");
                sleep();
                continue;
            }

            System.out.println("Philosopher " + name + " eating");
            sleep();


            Main.chopSticks[name] = left;
            Main.chopSticks[rightStickNum] = right;

            System.out.println("Philosopher " + name + " finish eating");
            break;
        }

    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "name='" + name + '\'' +
                '}';
    }
}
