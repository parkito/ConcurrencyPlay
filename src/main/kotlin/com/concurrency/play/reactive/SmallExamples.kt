package com.concurrency.play.reactive

import reactor.core.publisher.Flux


fun main() {
    fluxError()
}

//    1
//    2
//    Error: java.lang.RuntimeException: Got to 3
private fun fluxError() {
    Flux.range(1, 10)
            .map {
                if (it == 3) {
                    throw RuntimeException("Got to 3")
                }
                return@map it

            }
            .subscribe(
                    { println(it) },
                    { print("Error: $it") }
            )
}