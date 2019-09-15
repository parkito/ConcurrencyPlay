package com.concurrency.play.pattern.active.monitor.model;

import java.util.Objects;

import static com.concurrency.play.pattern.active.monitor.model.Status.NOT_STARTED;

public class Task {

    public String taskName;
    public Status status;
    public int processTime;

    public Task(String taskName, int processTime) {
        this.taskName = taskName;
        this.status = NOT_STARTED;
        this.processTime = processTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return processTime == task.processTime &&
                Objects.equals(taskName, task.taskName) &&
                status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, status, processTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", status=" + status +
                ", processTime=" + processTime +
                '}';
    }
}
