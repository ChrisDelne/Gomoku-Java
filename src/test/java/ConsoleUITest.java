import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;


public class ConsoleUITest {

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

    //prima vanno implementati i test di lettura

    // legge piu input validi
    @Test
    void Console_can_read_2_int_from_input() {
        Scanner in = new Scanner(new ByteArrayInputStream("10 20\n".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        assertEquals(new Position(10, 20), ui.readPosition(""));
    }


    @Test
    void readPosition_whenInputDoesNotMatchRegex_printsErrorAndRetries() {
        // Arrange: prima riga invalida, poi riga valida (così il metodo termina)
        String userInput = "ciao mondo\n10 20\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        // Act
        Position p = ui.readPosition("Inserisci mossa: ");

        // Assert sul valore ritornato
        assertEquals(new Position(10, 20), p);

        // Assert sull'output stampato
        String printed = outBuffer.toString(StandardCharsets.UTF_8);

        // 1) ha stampato almeno un messaggio di errore
        assertTrue(printed.contains("Input non valido"),
                "Mi aspetto un messaggio che segnali input non valido. Output:\n" + printed);

        // 2) (opzionale) ha mostrato il prompt almeno due volte (una per ogni tentativo)
        assertTrue(countOccurrences(printed, "Inserisci mossa: ") == 2,
                "Mi aspetto che il prompt venga mostrato almeno due volte. Output:\n" + printed);
    }

    @Test
    void readPosition_whenNumberTooLarge_printsRangeErrorAndRetries() {
        // Arrange: numero fuori range per int, poi riga valida
        // 9999999999999999999 non entra in int -> Integer.parseInt lancia NumberFormatException
        String userInput = "9999999999999999999 20\n10 20\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        // Act
        Position p = ui.readPosition("Inserisci mossa: ");

        // Assert sul valore ritornato
        assertEquals(new Position(10, 20), p);

        // Assert sull'output stampato
        String printed = outBuffer.toString(StandardCharsets.UTF_8);

        assertTrue(printed.contains("fuori range") || printed.contains("Valore fuori range"),
                "Mi aspetto un messaggio che segnali overflow/fuori range. Output:\n" + printed);

        // (opzionale) prompt almeno due volte
        assertTrue(countOccurrences(printed, "Inserisci mossa: ") == 2,
                "Mi aspetto che il prompt venga mostrato almeno due volte. Output:\n" + printed);
    }

    //scarta gli imput non validi
    @ParameterizedTest
    @CsvSource({
            "'abc\n10 20',          10,20",
            "'abc\n10,a\n10 20',    10,20",
            "'adf\nsdg\nfgj\n3 4',  3,4",
            "';\n,\nciao\n7 8',     7,8"
    })
    void readPosition_skips_any_number_of_invalid_inputs(String inputLines, int expectedRow, int expectedCol) {
        Scanner in = new Scanner(new ByteArrayInputStream((inputLines + "\n").getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);
        ConsoleUI ui = new ConsoleUI(in, out);

        Position pos = ui.readPosition("");
        assertEquals(new Position(expectedRow, expectedCol), pos);
    }


    //lancia eccezione EOF
    @Test
    void readPosition_throws_if_input_ends() {
        Scanner in = new Scanner(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ConsoleUI ui = new ConsoleUI(in, out);

        assertThrows(IllegalStateException.class, () -> ui.readPosition(""));
    }




    //ConsoleUI consoleUI = new ConsoleUI(new Scanner(System.in), System.out);
    @Disabled
    @Test
    void in_progres_make_move(){
        FakeGame game = new FakeGame()
                .withState(GameState.IN_PROGRESS);


        String userInput = "1 2\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        // da valutare
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);

        // Act
        consoleUI.use(game);

        // Assert: nessuna mossa applicata
        assertEquals(1, game.getMakeMoveCallCount(),
                "Se lo stato è IN_PROGRESS, ConsoleUI deve chiamare makeMove().");


    }



    //se lo stato non è in progress non applica la mossa
    @Disabled
    @Test
    void not_in_progres_no_move(){
        FakeGame game = new FakeGame()
                .withState(GameState.BLACK_WON);

        // Input "finto": anche se l'utente scrivesse una mossa, la UI non dovrebbe leggerla
        String userInput = "1 2\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        // Output catturato (opzionale, utile se vuoi controllare cosa stampa)
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);

        // Act
        consoleUI.use(game);

        // Assert: nessuna mossa applicata
        assertEquals(0, game.getMakeMoveCallCount(),
                "Se lo stato non è IN_PROGRESS, ConsoleUI non deve chiamare makeMove().");


    }


}
