package by.alex;

import by.alex.model.Accumulator;
import by.alex.model.Client;
import by.alex.model.Request;
import by.alex.model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {

        List<Request> requestList = new ArrayList<>();
        int n = 10; // пример: n = 10
        for (int i = 1; i <= n; i++) {
            requestList.add(new Request(i));
        }

        Accumulator accumulator = new Accumulator((1 + n) * (n / 2));

        Server server = new Server(requestList,  accumulator);
        Client client = new Client(requestList, server, accumulator);

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Void> serverFuture = executor.submit(server);
        Future<Void> clientFuture = executor.submit(client);

        try {
            serverFuture.get();
            clientFuture.get();
            System.out.println("Integration test result: " + (accumulator.getValue() == (1 + n) * (n / 2)));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
