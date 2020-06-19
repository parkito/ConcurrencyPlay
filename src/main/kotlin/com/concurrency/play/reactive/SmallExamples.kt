package com.concurrency.play.reactive

import reactor.core.publisher.Flux


fun main() {
    Flux.range(1, 10)
            .map {
                if (it == 3) {
                    throw RuntimeException("Got to 4")
                }
                return@map it

            }
            .subscribe(
                    { println(it) },
                    { print("Error: $it") }
            )
}