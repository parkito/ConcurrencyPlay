package com.concurrency.play.reactive

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Executors.newFixedThreadPool


fun main() {
    val scheduler = Schedulers.fromExecutor(newFixedThreadPool(10))
    val intro = Intro()

    val stream1 = intro.getStream()
    val stream2 = intro.getStream()
    val stream3 = intro.getStream()

    val flux1 = Flux.fromStream(stream1)
            .parallel()
            .runOn(scheduler)
            .doOnNext { intro.print(it) }

    println("HERE !!!!")

    val flux2 = Flux.fromStream(stream2)
            .parallel()
            .runOn(scheduler)
            .doOnNext { intro.print(it) }


    val flux3 = Flux.fromStream(stream3)
            .parallel()
            .runOn(scheduler)
            .doOnNext { intro.print(it) }


//    listOf(flux1, flux2, flux3).forEach { it.subscribe() }

    Mono.`when`(flux1, flux2, flux3).block()
    println("HERE 111")

}