package by.alex.model;


import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class Client implements Runnable {
    private final List<Integer> data;
    private final int n;
    private final ExecutorService executorService;
    private int accumulator;

    public Client(List<Integer> data, Accumulator accumulator) {
        this.data = data;
        this.n = data.size();
        this.executorService = Executors.newFixedThreadPool(n);
        this.accumulator = 0;
    }

    public void sendDataToServer(Server server) {
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int index = (int) (Math.random() * data.size());
            int value = data.remove(index);
            tasks.add(() -> {
                int responseSize = server.processRequest(value);
                return responseSize;
            });
        }
        try {
            List<Future<Integer>> futures = executorService.invokeAll(tasks);
            for (Future<Integer> future : futures) {
                accumulator += future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
        executorService.shutdown();
    }

    @Override
    public void run() {

    }
}

