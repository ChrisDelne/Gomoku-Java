import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    Grid grid = new Grid();


    @Test
    void getROWS() {
        assertEquals(15, grid.getROWS());
    }

    @Test
    void getCOLUMNS() {
        assertEquals(15, grid.getCOLUMNS());
    }


    @ParameterizedTest
    @CsvSource({
            "5, 5, true, false",
            "10, 14, true, false",
            "1, 5, true, false",
            "7, 7, false, false",
            "6, 3, false, false",
            "1, 1, false, true"
    })
    void isEmpty(int row, int column, boolean empty, boolean occupyWithBlack) {
        if (!empty && occupyWithBlack) grid.occupyCrossWithBlack(row, column);
        else
        if (!empty) grid.occupyCrossWithWhite(row, column);
        assertEquals(empty, grid.isEmpty(row, column));
    }


    @Test
    void occupyCrossWithBlack() {
        grid.occupyCrossWithBlack(4, 4);
        assertEquals(Grid.Cross.BLACK, grid.isOccupiedByBlack(4, 4) ? Grid.Cross.BLACK : Grid.Cross.EMPTY);
    }

    @Test
    void occupyCrossWithWhite() {
        grid.occupyCrossWithWhite(2, 2);
        assertEquals(Grid.Cross.WHITE, grid.isOccupiedByWhite(2, 2) ? Grid.Cross.WHITE : Grid.Cross.EMPTY);
    }

    @Test
    void isOccupiedByBlack() {
        grid.occupyCrossWithBlack(6, 6);
        assertEquals(true, grid.isOccupiedByBlack(6, 6));
    }

    @Test
    void isOccupiedByWhite() {
        grid.occupyCrossWithWhite(8, 8);
        assertEquals(true, grid.isOccupiedByWhite(8, 8));
    }

    @Test
    void getCrossAt() {
        grid.occupyCrossWithBlack(3, 3);
        assertEquals(Grid.Cross.BLACK, grid.getCrossAt(3, 3));
    }

    @Test
    void getCrossAtEmpty() {
        assertEquals(Grid.Cross.EMPTY, grid.getCrossAt(0, 0));
    }

    @Test
    void getCrossAtWhite() {
        grid.occupyCrossWithWhite(9, 9);
        assertEquals(Grid.Cross.WHITE, grid.getCrossAt(9, 9));
    }
}


