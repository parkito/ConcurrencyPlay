package com.siksmfp.concurrency.reactive;

import com.siksmfp.concurrency.algo.common.Threads;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

//You can use Schedulers.immediate() to obtain a Scheduler that runs code on the current, caller thread.
// Schedulers.parallel() is optimized for running fast, non-blocking executions.
// Schedulers.single() is optimized for low-latency one-off executions.
// Schedulers.elastic() is optimized for longer executions, and is an alternative for blocking
//     tasks where the number of active tasks and threads grow indefinitely. This is an unbounded thread pool.
// Schedulers.boundedElastic() is optimized for longer executions, and is an alternative for blocking tasks
//     where the number of active tasks (and threads) is capped. If none of these suit your use case,
//     you can always factory a new Scheduler using Schedulers.fromExecutorService(ExecutorService).

public class Scheduler {
    private final AtomicInteger methodInvocationCounts = new AtomicInteger();

    private String rsb = "rsb";

    @BeforeEach
    public void before() {
        Schedulers.resetFactory();
        Schedulers.addExecutorServiceDecorator(
                rsb,
                (scheduler, scheduledExecutorService) -> decorate(scheduledExecutorService)
        );
    }

    @Test
    public void changeDefaultDecorator() {
        Flux<Integer> integerFlux = Flux.just(1).delayElements(Duration.ofMillis(1));
        StepVerifier.create(integerFlux).thenAwait(Duration.ofMillis(10))
                .expectNextCount(1).verifyComplete();
        assertEquals(1, methodInvocationCounts.get());
    }

    @Test
    public void onScheduleHook() {
        var counter = new AtomicInteger();
        Schedulers.onScheduleHook("my hook",
                runnable -> () -> {
                    var threadName = Thread.currentThread().getName();
                    counter.incrementAndGet();
                    System.out.println("before execution: " + threadName);
                    runnable.run();
                    System.out.println("after execution: " + threadName);
                }
        );

        Flux<Integer> integerFlux = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(1))
                .subscribeOn(Schedulers.immediate());

        StepVerifier.create(integerFlux).expectNext(1, 2, 3).verifyComplete();
        assertEquals(3, counter.get());
    }

    @Test
    public void subscribeOn() {
        var rsbThreadName = "SOME NAME";
        var map = new ConcurrentHashMap<String, AtomicInteger>();
        var executor = Executors.newFixedThreadPool(
                5,
                runnable -> {
                    Runnable wrapper = () -> {
                        var key = Thread.currentThread().getName();
                        map.computeIfAbsent(key, s -> new AtomicInteger())
                                .incrementAndGet();
                        Threads.printThread(key);
                        runnable.run();
                    };
                    return new Thread(wrapper, rsbThreadName);
                }
        );
        reactor.core.scheduler.Scheduler scheduler = Schedulers.fromExecutor(executor);

        Mono<Integer> integerFlux = Mono.just(1)
                .subscribeOn(scheduler)
                .doFinally(signal -> map.forEach((k, v) -> System.out.println(k + '=' + v)));

        StepVerifier.create(integerFlux).expectNextCount(1).verifyComplete();
        var atomicInteger = map.get(rsbThreadName);
        Assertions.assertEquals(atomicInteger.get(), 1);
    }

    @AfterEach
    public void after() {
        Schedulers.resetFactory();
        Schedulers.removeExecutorServiceDecorator(this.rsb);
    }

    private ScheduledExecutorService decorate(ScheduledExecutorService executorService) {
        try {
            var pfb = new ProxyFactoryBean();
            pfb.setProxyInterfaces(new Class[]{ScheduledExecutorService.class});
            pfb.addAdvice(
                    (MethodInterceptor) methodInvocation -> {
                        var methodName = methodInvocation.getMethod().getName().toLowerCase();
                        methodInvocationCounts.incrementAndGet();
                        System.out.println("methodName: (" + methodName + ") incrementing...");
                        return methodInvocation.proceed();
                    }
            );
            pfb.setSingleton(true);
            pfb.setTarget(executorService);
            return (ScheduledExecutorService) pfb.getObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
