package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class Merge {

    //The merge function executes a merging of the data from Publisher
    //sequences contained in an array into an interleaved merged sequence.
    //opposed to concat (lazy subscription), the sources are subscribed eagerly
    @Test
    public void merge() {
        Flux<Integer> fastest = Flux.just(5, 6);
        Flux<Integer> secondFastest = Flux.just(1, 2).delayElements(Duration.ofMillis(2));
        Flux<Integer> thirdFastest = Flux.just(3, 4).delayElements(Duration.ofMillis(20));
        Flux<Flux<Integer>> streamOfStreams = Flux.just(secondFastest, thirdFastest, fastest);
        Flux<Integer> merge = Flux.merge(streamOfStreams);
        StepVerifier.create(merge).expectNext(5, 6, 1, 2, 3, 4).verifyComplete();
    }

    //Zip two sources together, that is to say wait for all the sources
    //to emit one element and combine these elements once into a Tuple2.
    //The operator will continue doing so until any of the sources completes.
    @Test
    public void zip() {
        Flux<Integer> first = Flux.just(1, 2, 3);
        Flux<String> second = Flux.just("a", "b", "c");
        Flux<String> zip = Flux.zip(first, second).
                map(this::from);
        StepVerifier.create(zip).expectNext("1:a", "2:b", "3:c").verifyComplete();
    }

    private String from(Tuple2 tuple2) {
        return tuple2.getT1() + ":" + tuple2.getT2();
    }
}
