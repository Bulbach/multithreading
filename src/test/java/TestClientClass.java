import by.alex.model.Accumulator;
import by.alex.model.Client;
import by.alex.model.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestClientClass {
    @Test
    public void testOriginalListAfterProcessingRequests() {
        // when
        Server server = new Server();
        Accumulator accumulator = new Accumulator();
        int originSizeList = 100;
        Client client = new Client(server, accumulator, originSizeList);

        // Given
        try {
            Integer result = client.call();
            System.out.println("Result after processing requests: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assert client.getOriginalList().isEmpty() : "originalList should be empty after processing requests";
    }

    @Test
    public void testValueFromAccumulatorWithTestValueAfterProcessingRequests(){

        // When
        Server server = new Server();
        Accumulator accumulator = new Accumulator();
        int originSizeList = 100;
        Client client = new Client(server, accumulator, originSizeList);
        int expected = (1 + originSizeList) * (originSizeList / 2);

        // Given
        try {
             client.call();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        Assertions.assertEquals(expected,accumulator.getValue());
    }
}
