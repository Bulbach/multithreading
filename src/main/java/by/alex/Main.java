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
        List<Integer> sharedResource = new ArrayList<>();
        int n = 16; // пример: n = 10
        for (int i = 1; i <= n; i++) {
            sharedResource.add(i);
        }

        Accumulator accumulator = new Accumulator();
        accumulator.setValue((1 + n) * (n / 2));

        Server server = new Server(sharedResource,accumulator);
        Client client = new Client(sharedResource, server,accumulator);

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> serverFuture = executor.submit(server);
        Future<Integer> clientFuture = executor.submit(client);

        try {
            serverFuture.get();
            int result = clientFuture.get();
            System.out.println("Integration test result: " + (result == accumulator.getValue()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
