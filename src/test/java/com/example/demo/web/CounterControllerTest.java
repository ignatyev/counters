package com.example.demo.web;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CounterControllerTest {

    CounterController counterController = new CounterController();

    @Test
    public void test() throws InterruptedException {
        int numberOfThreads = 4;
        int numberOfIncrements = 100;

        IntStream.range(0, 4).forEach(name -> {
            counterController.createCounter(String.valueOf(name));
        });

        Runnable accumulateAction = () -> IntStream
                .rangeClosed(0, numberOfThreads)
                .forEach(name -> {
                    counterController.increment(String.valueOf(name));
                });

        ExecutorService executorService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < numberOfIncrements; i++) {
            executorService.execute(accumulateAction);
        }
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(numberOfIncrements * numberOfThreads, counterController.getAll());
    }

}