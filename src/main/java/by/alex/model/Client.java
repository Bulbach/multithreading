package by.alex.model;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class Client implements Callable<Integer> {
    private final Server server;
    private final Accumulator accumulator;
    private final int originSizeList;
    @Getter
    private List<Integer> originalList = new ArrayList<>();

    public Client(Server server, Accumulator accumulator, int originSizeList) {

        this.server = server;
        this.accumulator = accumulator;
        this.originSizeList = originSizeList;
        for (int i = 1; i <= originSizeList; i++) {
            originalList.add(i);
        }
    }

    @Override
    public Integer call() {

        while (!originalList.isEmpty()) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (int i = 0; i < originalList.size(); i++) {

                final int value = originalList.remove(i);
                Request request = new Request();
                request.setValue(value);
                executorService.submit(() -> {
                    Response responseSize = server.processRequest(request);
                    accumulator.add(responseSize.getValue());
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

        }
        return accumulator.getValue();
    }
}
