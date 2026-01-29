import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public abstract class BaseConsoleTest {
    protected ByteArrayOutputStream outBuffer;
    protected PrintStream out;

    @BeforeEach
    void setUpStreams() {
        outBuffer = new ByteArrayOutputStream();
        out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

    }

    protected Scanner createScanner(String inputData) {
        return new Scanner(new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8)));
    }

    protected String getCapturedOutput() {
        return outBuffer.toString(StandardCharsets.UTF_8);
    }
}