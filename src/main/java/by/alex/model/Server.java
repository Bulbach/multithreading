package by.alex.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@RequiredArgsConstructor
public class Server {
    @Getter
    private List<Integer> commonList = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public Response processRequest(Request request) {
        int processingDelay = ThreadLocalRandom.current().nextInt(100, 1001);
        try {
            Thread.sleep(processingDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        lock.lock();
        try {
            commonList.add(request.getValue());
            Response response = new Response();
            response.setValue(commonList.size());
            return response;
        } finally {
            lock.unlock();
        }
    }
}
