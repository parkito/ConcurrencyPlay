package com.concurrency.play.reactive

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.reactivestreams.FlowAdapters
import reactor.adapter.JdkFlowAdapter
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST
import reactor.core.publisher.Sinks.EmitResult
import reactor.test.StepVerifier
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier
import java.util.stream.Stream


class Basics {

    val executorService = Executors.newFixedThreadPool(1)

    @Test
    fun createPublishers() {
        val now = System.currentTimeMillis()
        val greetingMono = Mono.just(Date(now))
        StepVerifier.create(greetingMono).expectNext(Date(now)).verifyComplete()

        val letters = Flux.just("A", "B", "C")
        StepVerifier.create(letters).expectNext("A", "B", "C").verifyComplete()

        val integer = AtomicInteger()
        val supplier = Supplier { integer.incrementAndGet() }
        val integerFlux = Flux.fromStream(Stream.generate(supplier))
        StepVerifier.create(integerFlux.take(3)).expectNext(1).expectNext(2).expectNext(3)
            .verifyComplete()
    }

    @Test
    fun jdkFlowConversion() {
        val original = Flux.range(0, 10)

        val rangeOfIntegersAsJdk9Flow = FlowAdapters
            .toFlowPublisher(original)
        val rangeOfIntegersAsReactiveStream = FlowAdapters
            .toPublisher(rangeOfIntegersAsJdk9Flow)

        StepVerifier.create(original).expectNextCount(10).verifyComplete()

        StepVerifier.create(rangeOfIntegersAsReactiveStream).expectNextCount(10)
            .verifyComplete()

        val rangeOfIntegersAsReactorFluxAgain = JdkFlowAdapter
            .flowPublisherToFlux(rangeOfIntegersAsJdk9Flow)
        StepVerifier.create(rangeOfIntegersAsReactorFluxAgain).expectNextCount(10)
            .verifyComplete()
    }

    @Test
    fun createFluxFromOuterWorld() {

        val integers = Flux.create { emitter: FluxSink<Int> -> launch(emitter, 5) }
        StepVerifier
            .create(integers.doFinally { executorService.shutdown() })
            .expectNextCount(5) //
            .verifyComplete()

    }

    private fun launch(integerFluxSink: FluxSink<Int>, count: Int) {
        executorService.submit {
            val integer = AtomicInteger()
            Assertions.assertNotNull(integerFluxSink)
            while (integer.get() < count) {
                val random = Math.random()
                integerFluxSink.next(integer.incrementAndGet())
                Thread.sleep((random * 1000).toLong())
            }
            integerFluxSink.complete()
        }
    }

    @Test
    fun emitterProcessor() {
        //Producers are deprecated use sinks instead
        val replaySink = Sinks.many().replay().all<Int>()
        replaySink.emitNext(1, FAIL_FAST)
        replaySink.emitNext(2, FAIL_FAST)
        val result: EmitResult = replaySink.tryEmitNext(3) //would return FAIL_NON_SERIALIZED
        result.isFailure

        val fluxView = replaySink.asFlux()
        fluxView
            .takeWhile { i: Int -> i < 10 }
            .log()
            .blockLast()
    }
}