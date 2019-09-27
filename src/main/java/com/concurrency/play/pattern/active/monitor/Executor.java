package com.concurrency.play.pattern.active.monitor;

import com.concurrency.play.pattern.active.monitor.model.Task;
import com.concurrency.play.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static com.concurrency.play.pattern.active.monitor.model.Status.FINISHED;

public class Executor {

    private java.util.concurrent.Executor executor;
    private BlockingQueue<Task> blockingQueue = new LinkedBlockingDeque<>();
    private BlockingQueue<Task> resultQueue = new LinkedBlockingDeque<>();

    public Executor(int threads) {
        this.executor = Executors.newFixedThreadPool(threads);
        executor.execute(this::execution);
    }

    public void execute(Task task) {
        blockingQueue.add(task);
    }

    public List<Task> getFinishedTasks() {
        List<Task> tasks = new ArrayList<>();
        resultQueue.drainTo(tasks);
        return tasks;
    }

    private void execution() {
        while (true) {
            try {
                Task task = blockingQueue.take();
                executor.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + ". Processing " + task);
                    Utils.sleepSeconds(task.getProcessTime());
                    task.setStatus(FINISHED);
                    resultQueue.add(task);
                    System.out.println("Processing finished " + task);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
