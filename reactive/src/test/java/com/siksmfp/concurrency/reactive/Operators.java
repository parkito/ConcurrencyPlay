package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Operators {

    //It gives you a chance to change the Publisher at assembly time, on initialization
    @Test
    public void transform() {
        var finished = new AtomicBoolean();
        var letters = Flux.just("A", "B", "C")
                .transform(
                        stringFlux -> stringFlux.doFinally(signal -> finished.set(true))
                );
        StepVerifier.create(letters).expectNextCount(3).verifyComplete();
        assertTrue(finished.get());
    }

    //then is the same but with Mono
    @Test
    public void thenMany() {
        var letters = new AtomicInteger();
        var numbers = new AtomicInteger();
        Flux<String> lettersPublisher = Flux.just("a", "b", "c")
                .doOnNext(value -> letters.incrementAndGet());
        Flux<Integer> numbersPublisher = Flux.just(1, 2, 3)
                .doOnNext(number -> numbers.incrementAndGet());
        //ignores lettersPublisher
        Flux<Integer> thisBeforeThat = lettersPublisher.thenMany(numbersPublisher);
        StepVerifier.create(thisBeforeThat).expectNext(1, 2, 3).verifyComplete();
        assertEquals(letters.get(), 3);
        assertEquals(numbers.get(), 3);
    }

    @Test
    public void maps() {
        var data = Flux.just("a", "b", "c").map(String::toUpperCase);
        StepVerifier.create(data).expectNext("A", "B", "C").verifyComplete();
    }

    //fastest wins
    @Test
    public void flatMap() {
        Flux<Integer> data = Flux.just(
                        new Pair(1, 300),
                        new Pair(2, 200),
                        new Pair(3, 100)
                )
                .flatMap(id -> delayReplyFor(id.id, id.delay));

        StepVerifier.create(data)
                .expectNext(3, 2, 1)
                .verifyComplete();
    }

    @Test
    public void concatMap() {
        Flux<Integer> data = Flux.just(
                        new Pair(1, 300),
                        new Pair(2, 200),
                        new Pair(3, 100)
                )
                .concatMap(id -> delayReplyFor(id.id, id.delay));

        StepVerifier.create(data)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    private Flux<Integer> delayReplyFor(Integer i, long delay) {
        return Flux.just(i).delayElements(ofMillis(delay));
    }

    //provides the most recent and drops previous
    @Test
    public void switchMapWithLookaheads() {
        Flux<String> source = Flux //
                .just("re", "rea", "reac", "react", "reactive") //
                .delayElements(Duration.ofMillis(100))//
                .switchMap(this::lookup);

        StepVerifier.create(source).expectNext("reactive").verifyComplete();
    }

    @Test
    public void take() {
        var count = 10;
        Flux<Integer> take = range().take(count);
        StepVerifier.create(take).expectNextCount(count).verifyComplete();
    }

    @Test
    public void takeUntil() {
        var count = 50;
        Flux<Integer> take = range().takeUntil(i -> i == (count - 1));
        StepVerifier.create(take).expectNextCount(count).verifyComplete();
    }

    @Test
    public void filter() {
        Flux<Integer> range = Flux.range(0, 1000).take(5);
        Flux<Integer> filter = range.filter(i -> i % 2 == 0);
        StepVerifier.create(filter).expectNext(0, 2, 4).verifyComplete();
    }


    private Flux<Integer> range() {
        return Flux.range(0, 1000);
    }

    private Flux<String> lookup(String word) {
        return Flux.just(word)//
                .delayElements(Duration.ofMillis(500));
    }


    static class Pair {
        private int id;
        private long delay;

        public Pair(int id, long delay) {
            this.id = id;
            this.delay = delay;
        }
    }
}
