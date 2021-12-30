package com.siksmfp.concurrency.reactive;

import com.siksmfp.concurrency.algo.common.Threads;
import org.junit.jupiter.api.Test;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.siksmfp.concurrency.algo.common.Rnd.random_5;
import static com.siksmfp.concurrency.algo.common.Threads.printThread;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Creation {

    @Test
    public void simple() {

        // <1>
        Publisher<Integer> rangeOfIntegers = Flux.range(0, 10);
        StepVerifier.create(rangeOfIntegers).expectNextCount(10).verifyComplete();

        // <2>
        Flux<String> letters = Flux.just("A", "B", "C");
        StepVerifier.create(letters).expectNext("A", "B", "C").verifyComplete();

        // <3>
        LocalTime now = LocalTime.now();
        Mono<LocalTime> greetingMono = Mono.just(LocalTime.ofNanoOfDay(now.getNano()));
        StepVerifier.create(greetingMono).expectNext(LocalTime.ofNanoOfDay(now.getNano())).verifyComplete();

        // <4>
        Mono<Object> empty = Mono.empty();
        StepVerifier.create(empty).verifyComplete();

        // <5>
        Flux<Integer> fromArray = Flux.fromArray(new Integer[]{1, 2, 3});
        StepVerifier.create(fromArray).expectNext(1, 2, 3).verifyComplete();

        // <6>
        Flux<Integer> fromIterable = Flux.fromIterable(Arrays.asList(1, 2, 3));
        StepVerifier.create(fromIterable).expectNext(1, 2, 3).verifyComplete();

        // <7>
        AtomicInteger integer = new AtomicInteger();
        Supplier<Integer> supplier = integer::incrementAndGet;
        Flux<Integer> integerFlux = Flux.fromStream(Stream.generate(supplier));
        StepVerifier.create(integerFlux.take(3)).expectNext(1).expectNext(2).expectNext(3)
                .verifyComplete();
    }

    @Test
    public void convert() {
        // <1>
        Flux<Integer> original = Flux.range(0, 10);

        Flow.Publisher<Integer> rangeOfIntegersAsJdk9Flow = FlowAdapters
                .toFlowPublisher(original);
        Publisher<Integer> rangeOfIntegersAsReactiveStream = FlowAdapters
                .toPublisher(rangeOfIntegersAsJdk9Flow);

        StepVerifier.create(original).expectNextCount(10).verifyComplete();

        StepVerifier.create(rangeOfIntegersAsReactiveStream).expectNextCount(10)
                .verifyComplete();

        // <2>
        Flux<Integer> rangeOfIntegersAsReactorFluxAgain = JdkFlowAdapter
                .flowPublisherToFlux(rangeOfIntegersAsJdk9Flow);
        StepVerifier.create(rangeOfIntegersAsReactorFluxAgain).expectNextCount(10)
                .verifyComplete();
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Test
    public void async() {
        Flux<Integer> integers = Flux.create(emitter -> launch(emitter, 5));

        StepVerifier
                .create(integers.doFinally(signalType -> executorService.shutdown()))
                .expectNextCount(5)//
                .verifyComplete();
    }

    // <3>
    private void launch(FluxSink<Integer> integerFluxSink, int limit) {
        executorService.submit(() -> {
            var integer = new AtomicInteger();
            assertNotNull(integerFluxSink);
            while (integer.get() < limit) {
                integerFluxSink.next(integer.incrementAndGet());
                printThread(integer.toString());
                Threads.sleepSeconds(random_5());
            }
            integerFluxSink.complete();
        });
    }
}
