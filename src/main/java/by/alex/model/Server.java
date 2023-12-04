package by.alex.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@RequiredArgsConstructor
public class Server implements Callable<Void> {

    private final List<Request> requestList;
    private final List<Integer> sharedResource = new CopyOnWriteArrayList<>();;
    private final Accumulator accumulator;
    private final Lock lock = new ReentrantLock();

    public Integer processRequest(Request request) throws InterruptedException {
        int value = request.getValue();
        int delay = (int) (Math.random() * 901) + 100; // задержка от 100 до 1000 мс
        Thread.sleep(delay);
        lock.lock();
        try {
            sharedResource.add(value);
            return sharedResource.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Void call() {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Integer>> futures = new ArrayList<>();

        for (Iterator<Request> iterator = requestList.iterator(); iterator.hasNext();) {
            Request request = iterator.next();
            Future<Integer> future = executor.submit(() -> processRequest(request));
            futures.add(future);
        }

        for (Future<Integer> future : futures) {
            try {
                future.get(); // дожидаемся выполнения всех запросов
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        accumulator.setValue(sharedResource.stream().mapToInt(Integer::intValue).sum());
        return null;
    }
}
