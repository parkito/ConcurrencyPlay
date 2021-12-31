package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static reactor.core.publisher.SignalType.ON_COMPLETE;

public class HotAndCold {

    @Test
    public void hot() throws Exception {
        var first = new ArrayList<Integer>();
        var second = new ArrayList<Integer>();
        EmitterProcessor<Integer> emitter = EmitterProcessor.create(2);
        FluxSink<Integer> sink = emitter.sink();
        emitter.subscribe(collect(first));
        sink.next(1);
        sink.next(2);
        emitter.subscribe(collect(second));
        sink.next(3);
        sink.complete();

        assertTrue(first.size() > second.size());
    }

    Consumer<Integer> collect(List<Integer> collection) {
        return collection::add;
    }


    @Test
    public void hot2() throws Exception {
        int factor = 10;
        System.out.println("start");
        var cdl = new CountDownLatch(2);
        Flux<Integer> live = Flux.range(0, 10).delayElements(Duration.ofMillis(factor))
                .share();

        var one = new ArrayList<Integer>();
        var two = new ArrayList<Integer>();
        live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(one));
        Thread.sleep(factor * 2);
        live.doFinally(signalTypeConsumer(cdl)).subscribe(collect(two));
        cdl.await(5, SECONDS);
        assertTrue(one.size() > two.size());
        System.out.println("First " + one.size());
        System.out.println("Second " + two.size());
        System.out.println("stop");
    }

    private Consumer<SignalType> signalTypeConsumer(CountDownLatch cdl) {
        return signal -> {
            if (signal.equals(ON_COMPLETE)) {
                try {
                    cdl.countDown();
                    System.out.println("await()...");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    public void pileUp() throws Exception {
        List<Integer> one = new ArrayList<>();
        List<Integer> two = new ArrayList<>();
        List<Integer> three = new ArrayList<>();

        Flux<Integer> pileOn = Flux.just(1, 2, 3)
                .publish()
                .autoConnect(3) //waits for subscribers
                .subscribeOn(Schedulers.immediate());

        pileOn.subscribe(subscribe(one));
        assertEquals(one.size(), 0);

        pileOn.subscribe(subscribe(two));
        assertEquals(two.size(), 0);

        pileOn.subscribe(subscribe(three));

        assertEquals(three.size(), 3);
        assertEquals(two.size(), 3);
        assertEquals(three.size(), 3);
    }

    private Consumer<Integer> subscribe(List<Integer> list) {
        return list::add;
    }
}
