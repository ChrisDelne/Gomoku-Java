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

    // =========================================================
    // lettura input
    // =========================================================

    // legge piu input validi
    @Test
    void Console_can_read_2_int_from_input() {
        Scanner in = new Scanner(new ByteArrayInputStream("10 20\n".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        assertEquals(new Position(10, 20), ui.readPosition(""));
    }


    @ParameterizedTest
    @CsvSource({
            "'a4\n10 20',                             10,20",
            "'5t - 43\n10 20',                        10,20",
            "'ciao 9999999999999999999\n3 4',         3,4",
            "'9999999999999999999 mondo\n7 8',        7,8",
            "'ciao mondo\n3 4',                       3,4",
            "';\n7 8',                                7,8"

    })
    void readPosition_whenInputDoesNotMatchRegex_printsErrorAndRetries(String input, int row, int col) {
        // Arrange: prima riga invalida, poi riga valida (così il metodo termina)
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        // Act
        Position p = ui.readPosition("Inserisci mossa: ");

        // Assert sul valore ritornato
        assertEquals(new Position(row, col), p);

        // Assert sull'output stampato
        String printed = outBuffer.toString(StandardCharsets.UTF_8);

        // 1) ha stampato almeno un messaggio di errore
        assertTrue(printed.contains("Input non valido"),
                "Mi aspetto un messaggio che segnali input non valido. Output:\n" + printed);

        // 2) (opzionale) ha mostrato il prompt almeno due volte (una per ogni tentativo)
        assertTrue(countOccurrences(printed, "Inserisci mossa: ") == 2,
                "Mi aspetto che il prompt venga mostrato due volte. Output:\n" + printed);
    }

    @ParameterizedTest
    @CsvSource({
            "'9999999999999999999 1\n10 20',                         10,20",
            "'3 9999999999999999999\n10 20',                         10,20",
            "'9999999999999999999  9999999999999999999\n3 4',         3,4",
            "'77777777777777777777 77777777777777777777\n7 8',        7,8"
    })
    void readPosition_whenNumberTooLarge_printsRangeErrorAndRetries(String userInput, int row, int col) {
        // Arrange: numero fuori range per int, poi riga valida
        // 9999999999999999999 non entra in int -> Integer.parseInt lancia NumberFormatException
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        // Act
        Position p = ui.readPosition("Inserisci mossa: ");

        // Assert sul valore ritornato
        assertEquals(new Position(row, col), p);

        // Assert sull'output stampato
        String printed = outBuffer.toString(StandardCharsets.UTF_8);

        assertTrue(printed.contains("fuori range") || printed.contains("Valore fuori range"),
                "Mi aspetto un messaggio che segnali overflow/fuori range. Output:\n" + printed);

        // (opzionale) prompt almeno due volte
        assertTrue(countOccurrences(printed, "Inserisci mossa: ") == 2,
                "Mi aspetto che il prompt venga mostrato due volte. Output:\n" + printed);
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

    //testare pure imput terminato

    // =========================================================
    // use
    // =========================================================


    //ConsoleUI consoleUI = new ConsoleUI(new Scanner(System.in), System.out);
    @Test
    void in_progres_make_move(){
        FakeGame game = new FakeGame()
                .withState(GameState.IN_PROGRESS)
                .endGameAfterMoves(3, GameState.BLACK_WON);


        String userInput = "1 2\n1 3\n1 4\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);

        // Act
        consoleUI.use(game);

        // Assert: nessuna mossa applicata
        assertEquals(3, game.getMakeMoveCallCount(),
                "Se lo stato è IN_PROGRESS, ConsoleUI deve chiamare makeMove().");


    }



    //se lo stato non è in progress non applica la mossa
    @ParameterizedTest
    @CsvSource({
            "BLACK_WON",
            "WHITE_WON",
            "DRAW"
    })

    void not_in_progres_no_move(GameState state){
        FakeGame game = new FakeGame()
                .withState(state);

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

    //test che amministra una mossa non valida per la logica di gioco
    @ParameterizedTest
    @CsvSource({
            "OUT_OF_BOUNDS",
            "POSITION_OCCUPIED"
    })
    void invalid_gameMove_in_gameLogic(MoveResult result){
        FakeGame game = new FakeGame()
                .withScriptedMoveResults(result, MoveResult.VALID_MOVE, result, MoveResult.VALID_MOVE)
                .endGameAfterMoves(4, GameState.BLACK_WON);


        String userInput = "0 -1\n 0, 0\n -3 -8\n 3 3\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        // Output catturato (opzionale, utile se vuoi controllare cosa stampa)
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);

        // Act
        consoleUI.use(game);

        // Assert sull'output stampato
        String printed = outBuffer.toString(StandardCharsets.UTF_8);

        assertEquals(2, //Non verifica più che stampi la frase ma che si verifichino due errori
                countOccurrences(printed, result.getReason()),
                "Mi aspetto che l'errore venga mostrato due volte. Output:\n" + printed
        );
        /*assertEquals(2, countOccurrences(printed, "una nuova posizione"),
                "Mi aspetto che il prompt di rihciesta nuova venga mostrato due volte. Output:\n" + printed);*/

    }

    @Test
    void use_printsMessage_and_exits_when_input_ends() {
        // Arrange: EOF immediato
        Scanner in = new Scanner(new ByteArrayInputStream(new byte[0]));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        FakeGame game = new FakeGame().withState(GameState.IN_PROGRESS);

        // Act + Assert: non deve propagare l'eccezione
        assertDoesNotThrow(() -> ui.use(game));

        // Assert: deve stampare il messaggio di uscita
        String printed = outBuffer.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Input terminato. Uscita dalla partita."),
                "Mi aspetto un messaggio di uscita quando l'input termina. Output:\n" + printed);
    }


}
