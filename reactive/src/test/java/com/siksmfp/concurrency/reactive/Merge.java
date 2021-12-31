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

    int min = 0;
    int max = 5;

    Flux<Integer> evenNumbers = Flux
            .range(min, max)
            .filter(x -> x % 2 == 0); // i.e. 2, 4

    Flux<Integer> oddNumbers = Flux
            .range(min, max)
            .filter(x -> x % 2 > 0);  // ie. 1, 3, 5

    //The concatenation is achieved by sequentially subscribing to the first source then waiting for it
    //to complete before subscribing to the next, and so on until the last source completes.
    //concatWith - just concatenation of 2 sources
    @Test
    public void contact() {
        Flux<Integer> fluxOfIntegers = Flux.concat(
                evenNumbers,
                oddNumbers
        );

        StepVerifier.create(fluxOfIntegers)
                .expectNext(0)
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectComplete()
                .verify();
    }

    //CombineLatest will generate data provided by the combination of the most recently published
    //values from each of the Publisher sources
    @Test
    public void combineLast() {
        Flux<Integer> fluxOfIntegers = Flux.combineLatest(
                evenNumbers,
                oddNumbers,
                Integer::sum
        );

        StepVerifier.create(fluxOfIntegers)
                .expectNext(5) // 4 + 1
                .expectNext(7) // 4 + 3
                .expectComplete()
                .verify();
    }

    @Test
    public void merge1() {
        Flux<Integer> fluxOfIntegers = Flux.merge(
                evenNumbers.delayElements(Duration.ofMillis(500L)),
                oddNumbers.delayElements(Duration.ofMillis(300L))
        );

        StepVerifier.create(fluxOfIntegers)
                .expectNext(1)
                .expectNext(0)
                .expectNext(3)
                .expectNext(2)
                .expectNext(4)
                .expectComplete()
                .verify();
    }

    //Unlike concat, sources are subscribed eagerly.
    //
    //Also, unlike merge, their emitted values are merged into the final sequence in subscription order
    @Test
    public void mergeSequential() {
        Flux<Integer> fluxOfIntegers = Flux.mergeSequential(
                evenNumbers,
                oddNumbers
        );

        StepVerifier.create(fluxOfIntegers)
                .expectNext(0)
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectComplete()
                .verify();
    }

    // This variant of the static merge method will delay any error until after
    // the rest of the merge backlog has been processed.
    @Test
    public void mergeDelayError() {
        Flux<Integer> fluxOfIntegers = Flux.mergeDelayError(1,
                evenNumbers.delayElements(Duration.ofMillis(500L)),
                oddNumbers.delayElements(Duration.ofMillis(300L))
        );

        StepVerifier.create(fluxOfIntegers)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(5)
                .expectNext(4)
                .expectComplete()
                .verify();
    }
}
