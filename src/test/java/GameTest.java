import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void gameStartsInProgressWithBlackTurn() {
        Game game = new Game();
        assertEquals(Game.GameState.IN_PROGRESS, game.getState());
        assertEquals(Player.BLACK, game.getCurrentPlayer());
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5",
            "7, 7",
            "10, 10",
            "0, 6"
    })
    void turnSwitchesAfterValidMove(int row, int col) {
        Game game = new Game();
        Grid.Position position = new Grid.Position(row, col);
        game.registerMove(position);
        assertEquals(Player.WHITE, game.getCurrentPlayer());
    }

    @ParameterizedTest
    @CsvSource({
            "OUT_OF_BOUNDS",
            "POSITION_OCCUPIED"
    })
    void turnDoesNotChangeAfterAnyInvalidMove(String scenario) {
        Game game = new Game();
        Player startingPlayer = game.getCurrentPlayer();
        MoveResult result;

        switch (scenario) {
            case "OUT_OF_BOUNDS" ->
                result = game.registerMove(new Grid.Position(-1, 1));
            case "POSITION_OCCUPIED" -> {
                Grid.Position pos = new Grid.Position(5, 5);
                game.registerMove(pos);              //Valida
                startingPlayer = game.getCurrentPlayer();
                result = game.registerMove(pos);     //Invalida
            }
            default -> throw new IllegalStateException();
        }

        assertNotEquals(MoveResult.VALID_MOVE, result);
        assertEquals(startingPlayer, game.getCurrentPlayer());
    }

    //Non so se ha senso, da valutare
    @ParameterizedTest
    @CsvSource({
            "0, 3",
            "2, 9",
            "14, 3",
            "10, 7"
    })
    void cannotMakeMoveIfGameIsFinished(int row, int col) {
        Game game = new Game();
        game.endGame();
        Grid.Position position = new Grid.Position(row, col);
        assertThrows(IllegalStateException.class, () -> game.registerMove(position));
    }

}

