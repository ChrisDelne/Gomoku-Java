import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleInputReaderTest {

    // Helper per contare quante volte una sottostringa compare nell'output
    private static int countOccurrences(String text, String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    // ------------ Lettura input: readPosition() ------------

    @ParameterizedTest
    @CsvSource({
            "100,20",
            "010,20",
            "3,4",
            "7,-8",
            "3,4",
            "-7,8"

    })
    void readPosition_canRead_2intFromInput(int row, int col) {
        String input = row + " " + col + "\n";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);

        ConsoleInputReader reader = new ConsoleInputReader(in, out);

        assertEquals(new Position(row - 1, col - 1), reader.readPosition(""));
    }

    @ParameterizedTest
    @CsvSource({
            "'9999999999999999999 1\n10 20',                         9,19",
            "'3 9999999999999999999\n10 20',                         9,19",
            "'9999999999999999999  9999999999999999999\n3 4',         2,3",
            "'3g 5\n7 8',                                             6,7",
            "'c\n7 8',                                                6,7",
            "'5\n8 9',                                                7,8"
    })
    void readPosition_readsInvalidThenValid_returnsExpectedPosition(String input, int row, int col) {
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleInputReader reader = new ConsoleInputReader(in, out);
        Position p = reader.readPosition("Inserisci mossa: ");

        assertEquals(new Position(row, col), p);
    }

    @ParameterizedTest
    @CsvSource({
            "'11\n100,20\n'",
            "'6g 5\n010,20\n'",
            "'33 g7\n3,4\n'",
            "'\n7,-8\n'",
            "'?\n-7,8\n'"
    })
    void readPosition_whenInputNotCorrectlyFormated_printsErrorMessage(String input) {
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleInputReader reader = new ConsoleInputReader(in, out);
        reader.readPosition("Inserisci mossa: ");

        String printed = outBuffer.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Input non valido"));
    }

    @Test
    void readPosition_whenInvalidThenValid_repeatsPromptTwice() {
        String input = "a4\n10 20\n";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleInputReader reader = new ConsoleInputReader(in, out);
        reader.readPosition("Inserisci mossa: ");

        String printed = outBuffer.toString(StandardCharsets.UTF_8);
        assertEquals(2, countOccurrences(printed, "Inserisci mossa: "));
    }

    @ParameterizedTest
    @CsvSource({
            "'9999999999999999999 1\n10 20\n'",
            "'1 9999999999999999999\n10 20\n'",
            "'77777777777777777777 77777777777777777777\n10 20\n'"
    })
    void readPosition_whenNumberTooLarge_doesNotThrow(String input) {
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);
        ConsoleInputReader reader = new ConsoleInputReader(in, out);

        assertDoesNotThrow(() -> reader.readPosition("Inserisci mossa: "));
    }

    @ParameterizedTest
    @CsvSource({
            "'abc\n10 20',          9,19",
            "'abc\n10,a\n10 20',    9,19",
            "'adf\nsdg\nfgj\n3 4',  2,3",
            "';\n,\nciao\n7 8',     6,7"
    })
    void readPosition_whenInvalidInputs_skipUntilValid(String inputLines, int expectedRow, int expectedCol) {
        Scanner in = new Scanner(new ByteArrayInputStream((inputLines + "\n").getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);
        ConsoleInputReader reader = new ConsoleInputReader(in, out);

        Position pos = reader.readPosition("");
        assertEquals(new Position(expectedRow, expectedCol), pos);
    }

    @Test
    void readPosition_ifInputEnds_throws() {
        Scanner in = new Scanner(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ConsoleInputReader reader = new ConsoleInputReader(in, out);

        assertThrows(InputTerminatedException.class, () -> reader.readPosition(""));
    }


}
