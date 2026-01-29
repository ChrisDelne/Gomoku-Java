import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final int WIN_LENGTH = 5;

    private enum InvalidMoveScenario {
        OUT_OF_BOUNDS,
        POSITION_OCCUPIED
    }

    // Helper: crea Position più leggibile
    private static Position p(int row, int col) {
        return new Position(row, col);
    }

    // Helper: imposta una vittoria del NERO in orizzontale in modo “dimension-agnostic”
    private static void makeBlackWinVertically(Game game) {
        GridView grid = game.getGrid();
        int rows = grid.getRows();
        int cols = grid.getColumns();

        int winRow = rows / 2;
        int startCol = (cols - WIN_LENGTH) / 2;

        // colonna per il bianco sicuramente fuori dal segmento [startCol, startCol+WIN_LENGTH-1]
        // se startCol == 0, la colonna 0 sarebbe dentro il segmento, quindi uso l’ultima colonna
        int whiteCol = (startCol == 0) ? (cols - 1) : 0;

        for (int i = 0; i < WIN_LENGTH; i++) {
            game.makeMove(p(winRow, startCol + i)); // BLACK
            if (i < WIN_LENGTH - 1) {
                game.makeMove(p(i, whiteCol));      // WHITE (mosse “innocue”)
            }
        }
    }

    // Helper: imposta una vittoria del BIANCO in verticale in modo “dimension-agnostic”
    private static void makeWhiteWinHorizontally(Game game) {
        GridView grid = game.getGrid();
        int rows = grid.getRows();
        int cols = grid.getColumns();

        int winCol = cols / 2;
        int startRow = (rows - WIN_LENGTH) / 2;

        // colonna per le mosse “di disturbo” del nero, diversa da winCol
        int blackCol = (winCol == 0) ? (cols - 1) : 0;

        // prima mossa NERA (disturbo)
        game.makeMove(p(rows -1, cols -1)); // BLACK

        for (int i = 0; i < WIN_LENGTH; i++) {
            game.makeMove(p(startRow + i, winCol));     // WHITE (linea vincente)
            if (i < WIN_LENGTH - 1)
                game.makeMove(p(i+1, blackCol));        // BLACK (disturbo)
        }
    }

    // ----- START STATE -----

    @Test
    void gameStartsInProgress() {
        Game game = new Game();
        assertEquals(GameState.IN_PROGRESS, game.getState());
    }

    @Test
    void gameStartsWithBlackTurn() {
        Game game = new Game();
        assertEquals(Player.BLACK, game.getCurrentPlayer());
    }

    // ----- TURN SWITCHING -----

    @ParameterizedTest
    @CsvSource({
            "5, 5",
            "7, 7",
            "10, 10",
            "0, 6"
    })
    void turnSwitchesAfterValidMove(int row, int col) {
        Game game = new Game();
        game.makeMove(p(row, col));
        assertEquals(Player.WHITE, game.getCurrentPlayer());
    }

    // Un test = un comportamento: qui verifico SOLO che la mossa è invalid
    @ParameterizedTest
    @EnumSource(InvalidMoveScenario.class)
    void invalidMoveIsNotValidMove(InvalidMoveScenario scenario) {
        Game game = new Game();

        MoveResult result = switch (scenario) {
            case OUT_OF_BOUNDS -> game.makeMove(p(-1, 1));
            case POSITION_OCCUPIED -> {
                Position pos = p(5, 5);
                game.makeMove(pos);   // valida (BLACK)
                yield game.makeMove(pos); // invalida (WHITE prova sulla stessa cella)
            }
        };

        assertNotEquals(MoveResult.VALID_MOVE, result);
    }

    // Un test = un comportamento: qui verifico SOLO che il turno non cambia dopo una mossa invalida
    @ParameterizedTest
    @EnumSource(InvalidMoveScenario.class)
    void turnDoesNotChangeAfterInvalidMove(InvalidMoveScenario scenario) {
        Game game = new Game();

        Player startingPlayer;
        switch (scenario) {
            case OUT_OF_BOUNDS -> {
                startingPlayer = game.getCurrentPlayer();
                game.makeMove(p(-1, 1));
            }
            case POSITION_OCCUPIED -> {
                Position pos = p(5, 5);
                game.makeMove(pos); // valida (BLACK) -> ora tocca a WHITE
                startingPlayer = game.getCurrentPlayer();
                game.makeMove(pos); // invalida
            }
            default -> throw new IllegalStateException();
        }

        assertEquals(startingPlayer, game.getCurrentPlayer());
    }

    // ----- FINAL STATES -----

    @Test
    void afterWinningMove_stateIsBlackWon() {
        Game game = new Game();
        makeBlackWinVertically(game);

        assertEquals(GameState.BLACK_WON, game.getState());
    }

    @Test
    void afterWinningMove_stateIsWhiteWon() {
        Game game = new Game();
        makeWhiteWinHorizontally(game);

        assertEquals(GameState.WHITE_WON, game.getState());
    }

    @Test
    void cannotMakeMoveIfGameFinished() {
        Game game = new Game();
        makeBlackWinVertically(game);

        assertThrows(IllegalStateException.class, () -> game.makeMove(p(5, 5)));
    }

}
