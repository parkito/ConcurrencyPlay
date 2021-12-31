package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

public class FlowControl {
    @Test
    public void timeout() {
        Flux<Integer> ids = Flux.just(1, 2, 3)
                .delayElements(Duration.ofSeconds(1))
                .timeout(Duration.ofMillis(500))
                .onErrorResume(this::given);
        StepVerifier.create(ids).expectNext(0).verifyComplete();
    }

    @Test
    public void first() {
        Flux<Integer> slow = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(10));
        Flux<Integer> fast = Flux.just(4, 5, 6, 7).delayElements(Duration.ofMillis(2));
        Flux<Integer> first = Flux.firstWithSignal(slow, fast);
        StepVerifier.create(first).expectNext(4, 5, 6, 7).verifyComplete();
    }

    private Flux<Integer> given(Throwable t) {
        Assertions.assertTrue(t instanceof TimeoutException);
        return Flux.just(0);
    }
}
