import by.alex.model.Accumulator;
import by.alex.model.Client;
import by.alex.model.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IntegrationTestClass {
    @Test
    public void testMultithreadedWorkWithClassClientAndServer() {
        // When
        int originSizeList = 100;
        Accumulator accumulator = new Accumulator();
        int expected = (1 + originSizeList) * (originSizeList / 2);
        Server server = new Server();
        Client client = new Client(server, accumulator, originSizeList);
        int actual = 0;
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> clientFuture = executor.submit(client);
        // Given
        try {
             actual = clientFuture.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        // Then
        Assertions.assertEquals(expected,actual);
    }
}
