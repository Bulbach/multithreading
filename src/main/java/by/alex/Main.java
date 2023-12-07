package by.alex;

import by.alex.model.Accumulator;
import by.alex.model.Client;
import by.alex.model.Server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        int originSizeList = 100;

        Accumulator accumulator = new Accumulator();
        int expected = (1 + originSizeList) * (originSizeList / 2);

        Server server = new Server();
        Client client = new Client(server, accumulator, originSizeList);

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> clientFuture = executor.submit(client);
        System.out.println(server.getCommonList());
        try {
            int result = clientFuture.get();
            System.out.println("result: " + result);
            System.out.println("Integration test result: " + (result == expected));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        server.getCommonList().stream().sorted().forEach(System.out::println);
    }
}
