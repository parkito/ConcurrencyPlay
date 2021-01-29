package com.concurrency.play.reactive

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function


class Operators {

    @Test
    fun transform() {
        val finished = AtomicBoolean()
        val letters = Flux
            .just("A", "B", "C")
            .transform { stringFlux: Flux<String> ->
                stringFlux.doFinally { finished.set(true) }
            }
        StepVerifier.create(letters).expectNextCount(3).verifyComplete()
        Assertions.assertTrue(finished.get())

        val filterAndMap = Function<Flux<String>, Flux<String>> { f ->
            f.filter { it != "orange" }
                .map { it.toUpperCase() }
        }

        Flux.fromIterable(listOf("blue", "green", "orange", "purple"))
            .doOnNext(::println)
            .transform(filterAndMap)
            .subscribe { println("Subscriber to Transformed MapAndFilter: $it") }
    }

    @Test
    fun transformDeferred() {
//      The transformDeferred operator is similar to transform and also lets you encapsulate operators in a function.
//      The major difference is that this function is applied to the original sequence on a per-subscriber basis.
//      It means that the function can actually produce a different operator chain for each subscription
//      (by maintaining some state).
        val ai = AtomicInteger()
        val filterAndMap = Function { f: Flux<String> ->
            if (ai.incrementAndGet() == 1) {
                f.filter { it != "orange" }
                    .map(String::toUpperCase)
            } else {
                f.filter { it != "purple" }
                    .map(String::toUpperCase)
            }
        }

        val composedFlux = Flux.fromIterable(listOf("blue", "green", "orange", "purple"))
            .doOnNext(::println)
            .transformDeferred(filterAndMap) //applied per subscription
//            .transform (filterAndMap) //per for flux

        composedFlux.subscribe { d: String -> println("Subscriber 1 to Composed MapAndFilter :$d") }
        composedFlux.subscribe { d: String -> println("Subscriber 2 to Composed MapAndFilter: $d") }
    }
}