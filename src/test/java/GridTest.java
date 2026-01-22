import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    Grid grid = new Grid();


    @ParameterizedTest
    @CsvSource({
            "3, 3",
            "0, 0",
            "14, 14",
            "7, 8",
            "10, 5"
    })
    void occupyCross(int row, int column) {
        grid.occupyCross(row, column);
        assertEquals(Grid.Cross.TAKEN, grid.isTaken(row, column) ? Grid.Cross.TAKEN : Grid.Cross.EMPTY);
    }


    @ParameterizedTest
    @CsvSource({
            "5, 5, true",
            "10, 14, true",
            "1, 5, true",
            "7, 7, false",
            "6, 3, false",
            "1, 1, false"
    })
    void isEmpty(int row, int column, boolean empty) {
        if (!empty) grid.occupyCross(row, column);

        assertEquals(empty, grid.isEmpty(row, column));
    }



    @ParameterizedTest
   @CsvSource({
           "5, 5, true",
           "10, 14, true",
           "1, 5, true",
           "7, 7, false",
           "6, 3, false",
           "1, 1, false"
   })
    void isTaken( int row, int column, boolean taken) {
        if (taken) grid.occupyCross(row, column);
        assertEquals(taken, grid.isTaken(row, column));
    }



}


