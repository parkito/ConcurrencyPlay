package com.concurrency.play.pattern.active.monitor;

import com.concurrency.play.pattern.active.monitor.model.Task;
import com.concurrency.play.utils.Utils;

public class Client {
    private Executor executor = new Executor(10);

    public void runProcessing() {
        for (int i = 0; i < 10; i++) {
            Task task = new Task(String.valueOf(i), i);
            executor.execute(task);
        }

        Utils.sleepSeconds(10);
        executor.getFinishedTasks()
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.runProcessing();
    }
}
