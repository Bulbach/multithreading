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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@RequiredArgsConstructor
public class Server implements Runnable{
    private final List<Integer> sharedResource;
    private final Lock lock;

    public Server(List<Integer> sharedResource) {
        this.sharedResource = sharedResource;
        this.lock = new ReentrantLock();
    }

    public int processRequest(int value) {
        int processingDelay = 100 + (int)(Math.random() * 901);  // случайная задержка от 100 до 1000 мс
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

    @Override
    public void run() {

    }
}
