import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    Grid grid = new Grid();

    @ParameterizedTest
    @CsvSource({
        "5,5",
        "10,14",
        "1,5"
    })
    public void EmptyCross(int row, int column) {
        assertEquals(true, grid.isEmpty(row, column));
    }


    @ParameterizedTest
    @CsvSource({
        "7,7",
        "6,3",
        "1,1"
    })
    public void notEmptyCross(int row, int column) {
        grid.occupyCell(row,column);
        assertEquals(false, grid.isEmpty(row, column));
    }

}
