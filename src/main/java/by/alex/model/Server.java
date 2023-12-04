package by.alex.model;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@RequiredArgsConstructor
public class Server implements Callable<Integer>{
    private final List<Integer> sharedResource;
    private final Accumulator accumulator;
    private final Lock lock = new ReentrantLock();

    public Integer call() {
        int sum = 0;
        for (int value : sharedResource) {
            sum += value;
        }
        accumulator.add(sum);
        return sum;
    }

    public int processRequest(int value) {
        int processingDelay = ThreadLocalRandom.current().nextInt(100, 1001); // случайная задержка от 100 до 1000 мс
        try {
            Thread.sleep(processingDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lock.lock();
        try {
            sharedResource.add(value);
            return sharedResource.size();
        } finally {
            lock.unlock();
        }
    }
}
