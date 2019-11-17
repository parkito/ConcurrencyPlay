package com.concurrency.play.exers.chap2;

import com.concurrency.play.utils.Utils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class ThreadConfinedTest {

    @Test()
    public void unsafeTest() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        ExecutorService executor = Executors.newFixedThreadPool(100);
        dfTesting(executor, sd::format);
        executor.shutdown();
        Utils.sleepSeconds(5);
    }

    @Test()
    public void threadConfinedTest() {
        ThreadConfined threadConfined = new ThreadConfined();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        dfTesting(executor, threadConfined::format);
        executor.shutdown();
        Utils.sleepSeconds(5);
    }

    @Test()
    public void stackConfinedTest() {
        StackConfined stackConfined = new StackConfined();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        dfTesting(executor, stackConfined::format);
        executor.shutdown();
        Utils.sleepSeconds(5);
    }

    @Test()
    public void objectConfinedTest() {
        ObjectConfined objectConfined = new ObjectConfined();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        dfTesting(executor, objectConfined::format);
        executor.shutdown();
        Utils.sleepSeconds(5);
    }

    @Test()
    public void immutableConfinedTest() {
        ImmutableParser immutableParser = new ImmutableParser();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        dfTesting(executor, immutableParser::format);
        executor.shutdown();
        Utils.sleepSeconds(5);
    }

    private void dfTesting(ExecutorService executor, Function<Date, String> function) {

        List<Date> dates = Arrays.asList(
                new Date(100000000),
                new Date(200000000),
                new Date(300000000),
                new Date(400000000),
                new Date(500000000)
        );

        List<String> parsedDates = Arrays.asList(
                "1970-01-01",
                "1970-01-03",
                "1970-01-04",
                "1970-01-05",
                "1970-01-06"
        );


        for (int i = 0; i < 100_000; i++) {
            executor.execute(
                    () -> {
                        for (Date date : dates) {
                            String result = function.apply(date);
                            if (!parsedDates.contains(result)) {
                                System.out.println("Error");
                                throw new IllegalStateException("Incorrect parsed date " + result);
                            }
                        }
                    }
            );
        }

    }
}
