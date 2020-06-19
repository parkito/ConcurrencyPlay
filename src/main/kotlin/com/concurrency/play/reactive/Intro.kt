package com.concurrency.play.reactive

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.util.stream.IntStream
import java.util.stream.Stream

class Intro {

    companion object {
        var counter = 0
    }

    fun getFlux(): Flux<String> {
        return Flux.fromStream(getStream())
    }

    fun getStream(): Stream<String> {
        val ctr = counter++
        return IntStream.rangeClosed(1, 100)
                .mapToObj { "$ctr ${Math.sqrt(Math.E * Math.PI * it)}" }
    }
}

fun main() {
    val intro = Intro()

    val flux1 = intro.getFlux().parallel().runOn(Schedulers.parallel())
    val flux2 = intro.getFlux()
    val flux3 = intro.getFlux()
    val flux4 = intro.getFlux()
    val flux5 = intro.getFlux()

    flux1.subscribe() {
        prnt(it)
        Thread.sleep(1000)

    }
    println("here1")
    flux2.subscribe { s -> prnt(s) }
    println("here2")
    flux3.subscribe { s -> prnt(s) }


//    listOf(flux1, flux2, flux3, flux4, flux5).forEach {
//        it.subscribe { s -> print(s) }
//        it.doFinally { print("finish") }
//    }
}

private fun prnt(str: String) {
    println("${Thread.currentThread().name} $str")
}
