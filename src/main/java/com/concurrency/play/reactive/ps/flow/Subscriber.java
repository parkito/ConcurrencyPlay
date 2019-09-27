package com.concurrency.play.reactive.ps.flow;

import java.util.concurrent.Flow;

public class Subscriber implements Flow.Subscriber<Message> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("Subscribed " + subscription);
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Message item) {
        System.out.println("Received " + item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error happened " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Completed");
    }
}
