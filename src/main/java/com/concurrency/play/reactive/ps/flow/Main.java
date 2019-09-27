package com.concurrency.play.reactive.ps.flow;

import com.concurrency.play.utils.Utils;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Main {
    public static void main(String[] args) {
        SubmissionPublisher<Message> publisher = new SubmissionPublisher<>();
        Flow.Subscriber<Message> subscriber1 = new Subscriber();

        publisher.subscribe(subscriber1);

        for (int i = 0; i < 100; i++) {
            Message message = new Message(String.valueOf(i));
            publisher.submit(message);
            System.out.println("Submitted "+message);
        }
        publisher.close();

        Utils.sleepSeconds(5);
    }
}
