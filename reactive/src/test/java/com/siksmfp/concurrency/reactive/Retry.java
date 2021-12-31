package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.time.Duration.ofSeconds;

public class Retry {

    @Test
    public void retry() {
        var errored = new AtomicBoolean();
        Flux<String> producer = Flux.create(sink -> {
            if (!errored.get()) {
                errored.set(true);
                sink.error(new RuntimeException("Nope!"));
                System.out.println("returning a " + RuntimeException.class.getName() + "!");
            } else {
                System.out.println("we've already errored so here's the value");
                sink.next("hello");
            }
            sink.complete();
        });
        Flux<String> retryOnError = producer.retryWhen(reactor.util.retry.Retry.backoff(2, ofSeconds(1)));
        StepVerifier.create(retryOnError).expectNext("hello").verifyComplete();
    }
}
