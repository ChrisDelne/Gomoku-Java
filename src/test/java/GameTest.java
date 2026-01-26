import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void gameStartsInProgressWithBlackTurn() {
        Game game = new Game();
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals(Player.BLACK, game.getCurrentPlayer());
    }

    //turn switching e not switching

    @ParameterizedTest
    @CsvSource({
            "5, 5",
            "7, 7",
            "10, 10",
            "0, 6"
    })
    void turnSwitchesAfterValidMove(int row, int col) {
        Game game = new Game();
        Position position = new Position(row, col);
        game.makeMove(position);
        assertEquals(Player.WHITE, game.getCurrentPlayer());
    }

    @ParameterizedTest
    @CsvSource({
            "OUT_OF_BOUNDS",
            "POSITION_OCCUPIED"
    })
    void turnDoesNotChangeAfterAnyInvalidMove(String scenario) {
        Game game = new Game();
        Player startingPlayer;
        MoveResult result;

        switch (scenario) {
            case "OUT_OF_BOUNDS" -> {
                startingPlayer = game.getCurrentPlayer();
                result = game.makeMove(new Position(-1, 1));
            }
            case "POSITION_OCCUPIED" -> {
                Position pos = new Position(5, 5);
                game.makeMove(pos);              //Valida
                startingPlayer = game.getCurrentPlayer();
                result = game.makeMove(pos);     //Non valida
            }
            default -> throw new IllegalStateException();
        }

        assertNotEquals(MoveResult.VALID_MOVE, result);
        assertEquals(startingPlayer, game.getCurrentPlayer());
    }

    //stati finali
    @Test
    void afterWinningMove_stateIsBlackWon() {
        Game game = new Game();

        game.makeMove(new Position(7, 7)); // B
        game.makeMove(new Position(0, 0)); // W
        game.makeMove(new Position(7, 8)); // B
        game.makeMove(new Position(1, 0)); // W
        game.makeMove(new Position(7, 9)); // B
        game.makeMove(new Position(2, 0)); // W
        game.makeMove(new Position(7, 10)); // B
        game.makeMove(new Position(3, 0)); // W
        game.makeMove(new Position(7, 11)); // B -> win

        assertEquals(GameState.BLACK_WON, game.getState());
    }

    @Test
    void afterWinningMove_stateIsWhiteWon() {
        Game game = new Game();

        game.makeMove(new Position(7, 7)); // B
        game.makeMove(new Position(0, 0)); // W
        game.makeMove(new Position(7, 8)); // B
        game.makeMove(new Position(1, 0)); // W
        game.makeMove(new Position(7, 9)); // B
        game.makeMove(new Position(2, 0)); // W
        game.makeMove(new Position(7, 10)); // B
        game.makeMove(new Position(3, 0)); // W
        game.makeMove(new Position(6, 10)); // B
        game.makeMove(new Position(4, 0)); // W -> win

        assertEquals(GameState.WHITE_WON, game.getState());
    }

    @Test
    void cannotMakeMoveIfGameFinished() {
        Game game = new Game();

        // BLACK vince
        game.makeMove(new Position(7, 7)); // B
        game.makeMove(new Position(0, 0)); // W
        game.makeMove(new Position(7, 8)); // B
        game.makeMove(new Position(1, 0)); // W
        game.makeMove(new Position(7, 9)); // B
        game.makeMove(new Position(2, 0)); // W
        game.makeMove(new Position(7, 10)); // B
        game.makeMove(new Position(3, 0)); // W
        game.makeMove(new Position(7, 11)); // B -> vince

        assertNotEquals(GameState.IN_PROGRESS, game.getState());

        assertThrows(IllegalStateException.class,
                () -> game.makeMove(new Position(5, 5)));
    }


}
