import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;


public class ConsoleUITest extends BaseConsoleTest{

    @Test
    void use_makeMoveAllowed_ifGameInProgress() {
        FakeGame game = new FakeGame()
                .withState(GameState.IN_PROGRESS)
                .endGameAfterMoves(3, GameState.BLACK_WON);

        Scanner in = createScanner("1 2\n1 3\n1 4\n");

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

        Scanner in = createScanner("1 2\n");

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

        Scanner in = createScanner("0 -1\n 1, 1\n -3 -8\n 3 3\n");
        ConsoleUI consoleUI = new ConsoleUI(in, out);

        consoleUI.use(game);

        String printed = getCapturedOutput(); //Output catturato
        assertEquals(2, //Verifica che avvengano due errori
                TestHelper.countOccurrences(printed, result.getReason()),
                "Mi aspetto che l'errore venga mostrato due volte. Output:\n" + printed);
    }

    @Test
    void use_whenInputEnds_printsMessageAndExits() {
        // Arrange: EOF immediato
        Scanner in = createScanner("");
        ConsoleUI ui = new ConsoleUI(in, out);
        FakeGame game = new FakeGame().withState(GameState.IN_PROGRESS);

        // Act + Assert: non deve propagare l'eccezione
        assertDoesNotThrow(() -> ui.use(game));

        // Assert: deve stampare il messaggio di uscita
        String printed = getCapturedOutput();
        assertTrue(printed.contains("Input terminato. Uscita dalla partita."),
                "Mi aspetto un messaggio di uscita quando l'input termina. Output:\n" + printed);
    }


}
