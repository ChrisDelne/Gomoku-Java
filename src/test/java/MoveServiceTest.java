import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveServiceTest {

    MoveService moveService = new MoveService(new Grid());

    @ParameterizedTest
    @CsvSource({
            "0, 0, OUT_OF_BOUNDS",
            "15, 15, OUT_OF_BOUNDS",
            "-1, 5, OUT_OF_BOUNDS",
            "5, -1, OUT_OF_BOUNDS"
    })
    void makeMoveOutOfBound(int row, int col, MoveResult expectedResult) {
        Grid.Position position = new Grid.Position(row, col);
        MoveResult result = moveService.makeMove(Player.BLACK, position);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5, VALID_MOVE",
            "7, 7, VALID_MOVE",
            "10, 10, VALID_MOVE",
            "0, 6, VALID_MOVE"
    })
    void makeMoveValid(int row, int col, MoveResult expectedResult) {
        Grid.Position position = new Grid.Position(row, col);
        MoveResult result = moveService.makeMove(Player.WHITE, position);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @CsvSource({
            "3, 3, POSITION_OCCUPIED",
            "4, 5, POSITION_OCCUPIED",
            "8, 2, POSITION_OCCUPIED",
            "1, 9, POSITION_OCCUPIED"
    })
    void makeMovePositionOccupied(int row, int col, MoveResult expectedResult) {
        Grid.Position position = new Grid.Position(row, col);
        moveService.makeMove(Player.BLACK, position); //Occupo la posizione
        MoveResult result = moveService.makeMove(Player.WHITE, position); //Dovrebbe essere occupata
        assertEquals(expectedResult, result);
    }
}
