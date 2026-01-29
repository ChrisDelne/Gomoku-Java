import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

        ConsoleUI ui = new ConsoleUI(in, out);

        assertEquals(new Position(row - 1, col - 1), ui.readPosition(""));
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

        ConsoleUI ui = new ConsoleUI(in, out);
        Position p = ui.readPosition("Inserisci mossa: ");

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

        ConsoleUI ui = new ConsoleUI(in, out);
        ui.readPosition("Inserisci mossa: ");

        String printed = outBuffer.toString(StandardCharsets.UTF_8);
        assertTrue(printed.contains("Input non valido"));
    }

    @Test
    void readPosition_whenInvalidThenValid_repeatsPromptTwice() {
        String input = "a4\n10 20\n";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);
        ui.readPosition("Inserisci mossa: ");

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
        ConsoleUI ui = new ConsoleUI(in, out);

        assertDoesNotThrow(() -> ui.readPosition("Inserisci mossa: "));
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
        ConsoleUI ui = new ConsoleUI(in, out);

        Position pos = ui.readPosition("");
        assertEquals(new Position(expectedRow, expectedCol), pos);
    }

    @Test
    void readPosition_ifInputEnds_throws() {
        Scanner in = new Scanner(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ConsoleUI ui = new ConsoleUI(in, out);

        assertThrows(InputTerminatedException.class, () -> ui.readPosition(""));
    }


    // ------------ Connessione al gioco: use ------------

    @Test
    void use_makeMoveAllowed_ifGameInProgress() {
        FakeGame game = new FakeGame()
                .withState(GameState.IN_PROGRESS)
                .endGameAfterMoves(3, GameState.BLACK_WON);

        String userInput = "1 2\n1 3\n1 4\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);
        consoleUI.use(game);

        // Assert: nessuna mossa applicata
        assertEquals(3, game.getMakeMoveCallCount(),
                "Se lo stato è IN_PROGRESS, ConsoleUI deve chiamare makeMove().");
    }


    @ParameterizedTest
    @CsvSource({
            "BLACK_WON",
            "WHITE_WON",
            "DRAW"
    })
    void use_makeMoveNotAllowed_ifGameNotInProgress(GameState state) {
        FakeGame game = new FakeGame()
                .withState(state);

        String userInput = "1 2\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream(); // Cattura output opzionale per controllo di stampa
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);
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
    void use_invalidMoveManagement_inGameLogic(MoveResult result) {
        FakeGame game = new FakeGame()
                .withScriptedMoveResults(result, MoveResult.VALID_MOVE, result, MoveResult.VALID_MOVE)
                .endGameAfterMoves(4, GameState.BLACK_WON);

        String userInput = "0 -1\n 1, 1\n -3 -8\n 3 3\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream(); // Cattura output opzionale per controllo di stampa
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);
        consoleUI.use(game);

        String printed = outBuffer.toString(StandardCharsets.UTF_8); //Output catturato
        assertEquals(2, //Verifica che avvengano due errori
                countOccurrences(printed, result.getReason()),
                "Mi aspetto che l'errore venga mostrato due volte. Output:\n" + printed);
    }

    @Test
    void use_whenInputEnds_printsMessageAndExits() {
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
