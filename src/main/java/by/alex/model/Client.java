package by.alex.model;


import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class Client implements Callable<Integer> {
    private final List<Integer> data;
    private final Server server;
    private final Accumulator accumulator;

    @Override
    public Integer call() {

        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Integer> dataCopy = new ArrayList(data);

        for (int value : dataCopy) {
            executorService.submit(() -> {
                int responseSize = server.processRequest(value);
                accumulator.add(responseSize);
            });
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return accumulator.getValue();
    }
}

