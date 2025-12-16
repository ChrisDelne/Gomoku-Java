import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    Grid grid = new Grid();

    @ParameterizedTest
    @CsvSource({
        "3,3,(3;3)",
        "13,1,(13;1)",
        "11,15,(11;15)"
    })
    public void printCoordinates(int x, int y, String expected) {
        assertEquals(expected, Grid.returnCoordinatesOF(x, y));
    }

    @ParameterizedTest
    @CsvSource({
        "5,5",
        "10,14",
        "1,5"
    })
    public void EmptyCross(int row, int column) {
        assertEquals(true, grid.isEmpty(row, column));
    }


    @Disabled
    @Test
    public void notEmpty7x7() {
        assertEquals(false, grid.isEmpty(7, 7));
    }
    @Disabled
    @Test
    public void notEmpty6x3() {
        assertEquals(false, grid.isEmpty(6, 3));
    }
    @Disabled
    @Test
    public void notEmpty1x1() {
        assertEquals(false, grid.isEmpty(1, 1));
    }

}
