package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.util.context.ContextView;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static reactor.core.publisher.SignalType.ON_NEXT;

//Thread like approach
public class Context {

    @Test
    public void context() throws Exception {
        var observedContextValues = new ConcurrentHashMap<String, AtomicInteger>();
        var max = 3;
        var key = "key1";
        var cdl = new CountDownLatch(max);
        reactor.util.context.Context context = reactor.util.context.Context.of(key, "value1");
        Flux<Integer> just = Flux.range(0, max)
                .delayElements(Duration.ofMillis(1))
                .doOnEach(
                        (Signal<Integer> integerSignal) -> {
                            ContextView currentContext = integerSignal.getContextView();
                            if (integerSignal.getType().equals(ON_NEXT)) {
                                String key1 = currentContext.get(key);
                                assertNotNull(key1);
                                assertEquals(key1, "value1");
                                observedContextValues.computeIfAbsent("key1", k -> new AtomicInteger(0))
                                        .incrementAndGet();
                            }
                        }
                )
                .contextWrite(context);

        just.subscribe(integer -> {
                    System.out.println("integer: " + integer);
                    cdl.countDown();
                }
        );

        cdl.await();
        assertEquals(observedContextValues.get(key).get(), max);

    }
}
