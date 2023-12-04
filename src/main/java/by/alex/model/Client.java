package by.alex.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class Client implements Callable<Void> {
    private final List<Request> requestList;
    private final Server server;
    private final Accumulator accumulator;

    @Override
    public Void call() throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Integer>> futures = new ArrayList<>();

        for (Request request : requestList) {
            int index = new Random().nextInt(requestList.size());
            int removed = requestList.remove(index).getValue();
            try {
                Thread.sleep(new Random().nextInt(401) + 100); // случайная задержка от 100 до 500 мс
                server.processRequest(request);
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }


        for (Future<Integer> future : futures) {
            try {
                accumulator.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return null;
    }
}

