package com.concurrency.play.pattern.balking;

//Not so good approach. Caller doesn't know job is executed or not
public class Job {
    private boolean jobInProgress = false;

    public void doJob() {
        synchronized (this) {
            if (jobInProgress) {
                return;
            }
            jobInProgress = true;
        }
        // Code to execute job goes here
        // ...
        jobCompleted();
    }

    private void jobCompleted() {
        synchronized (this) {
            jobInProgress = false;
        }
    }
}
