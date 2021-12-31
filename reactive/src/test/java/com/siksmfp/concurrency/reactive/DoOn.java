package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;

public class DoOn {

    @Test
    public void doOn() {
        var signals = new ArrayList<Signal<Integer>>();
        var nextValues = new ArrayList<Integer>();
        var subscriptions = new ArrayList<Subscription>();
        var exceptions = new ArrayList<Throwable>();
        var finallySignals = new ArrayList<SignalType>();

        Flux<Integer> on = Flux.<Integer>create(
                        sink -> {
                            sink.next(1);
                            sink.next(2);
                            sink.next(3);
                            sink.error(new IllegalArgumentException("oops!"));
                            sink.complete();
                        }
                )
                .doOnNext(nextValues::add) //everything
                .doOnEach(signals::add)//includes exceptions
                .doOnSubscribe(subscriptions::add)//
                .doOnError(IllegalArgumentException.class, exceptions::add)//
                .doFinally(finallySignals::add);

        StepVerifier//
                .create(on)//
                .expectNext(1, 2, 3)//
                .expectError(IllegalArgumentException.class)//
                .verify();

        signals.forEach(System.out::print);
        Assertions.assertEquals(4, signals.size());

        finallySignals.forEach(System.out::print);
        Assertions.assertEquals(finallySignals.size(), 1);

        subscriptions.forEach(System.out::print);
        Assertions.assertEquals(subscriptions.size(), 1);

        exceptions.forEach(System.out::print);
        Assertions.assertEquals(exceptions.size(), 1);
        Assertions.assertTrue(exceptions.get(0) instanceof IllegalArgumentException);

        nextValues.forEach(System.out::print);
        Assertions.assertEquals(Arrays.asList(1, 2, 3), nextValues);
    }
}
