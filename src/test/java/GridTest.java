import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class GridTest {

    //griglia 15x15
    private Grid grid;

    //per sicurezza forziamo il reset per ogni test
    @BeforeEach
    void setUp() {
        grid = new Grid();
    }

    //proprietà intrinseche

    // -----------------------------
    // Dimension getters
    // -----------------------------

    @Test
    void getROWS_return_15() {
        assertEquals(15, grid.getROWS());
    }

    @Test
    void getCOLUMNS_return_15() {
        assertEquals(15, grid.getCOLUMNS());
    }

    // -----------------------------
    // griglia inizializzata vuota
    // -----------------------------

    @Test
    void new_grid_is_all_empty() {

        boolean allEmpty =
                IntStream.range(0, grid.getROWS()).allMatch(r ->
                        IntStream.range(0, grid.getCOLUMNS()).allMatch(c ->
                                grid.getCrossAt(r, c) == CrossState.EMPTY));

        assertTrue(allEmpty);
    }

    // -----------------------------
    // contains(int, int)
    // -----------------------------

    //Contains
    @ParameterizedTest
    @CsvSource({
            "0,0,true",
            "14,14,true",
            "-1,0,false",
            "0,-1,false",
            "15,0,false",
            "0,15,false"
    })
    void contains_cases(int row, int col, boolean expected) {
        assertEquals(expected, grid.contains(row, col));
    }

    //contains (position),
    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,14",
            "-1,30",
            "0,-1",
            "15,0",
            "0,15"
    })
    void contains_position_equals_contains_int_int(int row, int col) {
        Position p = new Position(row, col);
        assertEquals(grid.contains(row, col), grid.contains(p));
    }

    // -----------------------------
    // scrittura e lettura stato
    // -----------------------------

    @Test
    void setBlackAt_with_int_readable_by_getCrossAt() {
        grid.setBlackAt(3, 3);
        assertEquals(CrossState.BLACK, grid.getCrossAt(3, 3));
    }

    @Test
    void setWhiteAt_with_int_readable_by_getCrossAt() {
        grid.setWhiteAt(9, 9);
        assertEquals(CrossState.WHITE, grid.getCrossAt(9, 9));
    }

    // --- isAt / isEmpty / isBlackAt / isWhiteAt (int,int) ---


    @ParameterizedTest
    @CsvSource({
            // actual, requested, expected
            "EMPTY, EMPTY, true",
            "EMPTY, BLACK, false",
            "EMPTY, WHITE, false",
            "BLACK, EMPTY, false",
            "BLACK, BLACK, true",
            "BLACK, WHITE, false",
            "WHITE, EMPTY, false",
            "WHITE, BLACK, false",
            "WHITE, WHITE, true"
    })
    void isAt_all_combinations(String actual, String requested, boolean expected) {
        int r = 2, c = 5; // non simmetrico, va bene

        CrossState actualState = CrossState.valueOf(actual);
        CrossState requestedState = CrossState.valueOf(requested);

        // arrange: imposta lo stato reale della cella
        if (actualState == CrossState.BLACK) grid.setBlackAt(r, c);
        if (actualState == CrossState.WHITE) grid.setWhiteAt(r, c);
        // se EMPTY non fare nulla

        assertEquals(expected, grid.isAt(requestedState, r, c));
    }

    @Test
    void isEmpty_false_after_setBlackAt() {
        grid.setBlackAt(1, 1);
        assertEquals(false, grid.isEmpty(1, 1));
    }

    @Test
    void isBlackAt_true_after_setBlackAt() {
        grid.setBlackAt(7, 7);
        assertEquals(true, grid.isBlackAt(7, 7));
    }

    @Test
    void isBlackAt_false_when_cell_is_white() {
        grid.setWhiteAt(2, 5);
        assertEquals(false, grid.isBlackAt(2, 5));
    }

    @Test
    void isWhiteAt_true_after_setWhiteAt() {
        grid.setWhiteAt(8, 8);
        assertEquals(true, grid.isWhiteAt(8, 8));
    }

    @Test
    void isWhiteAt_false_when_cell_is_black() {
        grid.setBlackAt(2, 5);
        assertEquals(false, grid.isWhiteAt(2, 5));
    }

    // --- Overload Position: getCrossAt(Position) ---

    @Test
    void getCrossAt_position_equals_getCrossAt_int_int() {
        Position p = new Position(2, 2);
        grid.setBlackAt(2, 2);
        assertEquals(grid.getCrossAt(2, 2), grid.getCrossAt(p));
    }

    // --- Overload Position: setter ---

    @Test
    void setBlackAt_position_writes_BLACK() {
        Position p = new Position(10, 10);
        grid.setBlackAt(p);
        assertEquals(CrossState.BLACK, grid.getCrossAt(10, 10));
    }

    @Test
    void setWhiteAt_position_writes_WHITE() {
        Position p = new Position(11, 11);
        grid.setWhiteAt(p);
        assertEquals(CrossState.WHITE, grid.getCrossAt(11, 11));
    }

    // --- Overload Position: isAt / isEmpty / isBlackAt / isWhiteAt ---

    @Test
    void isAt_position_true_when_state_matches() {
        Position p = new Position(12, 12);
        grid.setWhiteAt(p);
        assertEquals(true, grid.isAt(CrossState.WHITE, p));
    }

    @Test
    void isEmpty_position_true_on_new_grid() {
        Position p = new Position(0, 0);
        assertEquals(grid.isEmpty(0, 0), grid.isEmpty(p));
    }

    @Test
    void isBlackAt_position_true_after_setBlackAt() {
        Position p = new Position(13, 13);
        grid.setBlackAt(p);
        assertEquals(grid.isBlackAt(13,13), grid.isBlackAt(p));
    }

    @Test
    void isWhiteAt_position_true_after_setWhiteAt() {
        Position p = new Position(14, 14);
        grid.setWhiteAt(p);
        assertEquals(grid.isWhiteAt(14,14), grid.isWhiteAt(p));
    }
}



