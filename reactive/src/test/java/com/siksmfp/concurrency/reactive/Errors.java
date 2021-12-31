package com.siksmfp.concurrency.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Errors {

    private final Flux<Integer> resultsInError = Flux.just(1, 2, 3)
            .flatMap(
                    counter -> {
                        if (counter == 2) {
                            return Flux.error(new IllegalArgumentException("Oops!"));
                        } else {
                            return Flux.just(counter);
                        }
                    }
            );

    @Test
    public void onErrorResume() {
        Flux<Integer> integerFlux = resultsInError
                //subscribe again on error
                .onErrorResume(IllegalArgumentException.class, e -> Flux.just(0, 0, 0));
        StepVerifier.create(integerFlux).expectNext(1, 0, 0, 0).verifyComplete();
    }

    @Test
    public void onErrorReturn() {
        Flux<Integer> integerFlux = resultsInError.onErrorReturn(0);
        StepVerifier.create(integerFlux).expectNext(1, 0).verifyComplete();
    }

    @Test
    public void onErrorMap() throws Exception {
        class GenericException extends RuntimeException {

        }
        var counter = new AtomicInteger();
        Flux<Integer> resultsInError = Flux.error(new IllegalArgumentException("oops!"));
        Flux<Integer> errorHandlingStream = resultsInError
                .onErrorMap(IllegalArgumentException.class, ex -> new GenericException())//change the error
                .doOnError(GenericException.class, ge -> counter.incrementAndGet());
        StepVerifier.create(errorHandlingStream).expectError().verify();
        assertEquals(counter.get(), 1);
    }
}
