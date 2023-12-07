import by.alex.model.Accumulator;
import by.alex.model.Client;
import by.alex.model.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestServerClass {
    @Test
    public void testListValueAfterProcessingRequests() {

        // When
        Server server = new Server();
        Accumulator accumulator = new Accumulator();
        int originSizeList = 100;
        Client client = new Client(server, accumulator, originSizeList);
        List<Integer> expected = new ArrayList<>();
        for (int i = 1; i <= originSizeList; i++) {
            expected.add(i);
        }

        // Given
        try {
            client.call();

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Integer> actual = server.getCommonList().stream().sorted().toList();

        // Then
        Assertions.assertEquals(expected, actual);
    }
}
