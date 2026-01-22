import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    Grid grid = new Grid();

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
        if (!empty)
            grid.occupyCell(row, column);

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
        if (taken)
            grid.occupyCell(row, column);

        assertEquals(taken, grid.isTaken(row, column));
    }
}
