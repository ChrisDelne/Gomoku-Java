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
                                grid.getCrossAt(r, c) == Grid.CrossState.EMPTY));

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
        Grid.Position p = new Grid.Position(row, col);
        assertEquals(grid.contains(row, col), grid.contains(p));
    }

    // -----------------------------
    // scrittura e lettura stato
    // -----------------------------

    @Test
    void setBlackAt_int_writes_BLACK_readable_by_getCrossAt() {
        grid.setBlackAt(3, 3);
        assertEquals(Grid.CrossState.BLACK, grid.getCrossAt(3, 3));
    }

    @Test
    void setWhiteAt_int_writes_WHITE_readable_by_getCrossAt() {
        grid.setWhiteAt(9, 9);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(9, 9));
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

        Grid.CrossState actualState = Grid.CrossState.valueOf(actual);
        Grid.CrossState requestedState = Grid.CrossState.valueOf(requested);

        // arrange: imposta lo stato reale della cella
        if (actualState == Grid.CrossState.BLACK) grid.setBlackAt(r, c);
        if (actualState == Grid.CrossState.WHITE) grid.setWhiteAt(r, c);
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
        Grid.Position p = new Grid.Position(2, 2);
        grid.setBlackAt(2, 2);
        assertEquals(grid.getCrossAt(2, 2), grid.getCrossAt(p));
    }

    // --- Overload Position: setter ---

    @Test
    void setBlackAt_position_writes_BLACK() {
        Grid.Position p = new Grid.Position(10, 10);
        grid.setBlackAt(p);
        assertEquals(Grid.CrossState.BLACK, grid.getCrossAt(10, 10));
    }

    @Test
    void setWhiteAt_position_writes_WHITE() {
        Grid.Position p = new Grid.Position(11, 11);
        grid.setWhiteAt(p);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(11, 11));
    }

    // --- Overload Position: isAt / isEmpty / isBlackAt / isWhiteAt ---

    @Test
    void isAt_position_true_when_state_matches() {
        Grid.Position p = new Grid.Position(12, 12);
        grid.setWhiteAt(p);
        assertEquals(true, grid.isAt(Grid.CrossState.WHITE, p));
    }

    @Test
    void isEmpty_position_true_on_new_grid() {
        Grid.Position p = new Grid.Position(0, 0);
        assertEquals(grid.isEmpty(0, 0), grid.isEmpty(p));
    }

    @Test
    void isBlackAt_position_true_after_setBlackAt() {
        Grid.Position p = new Grid.Position(13, 13);
        grid.setBlackAt(p);
        assertEquals(grid.isBlackAt(13,13), grid.isBlackAt(p));
    }

    @Test
    void isWhiteAt_position_true_after_setWhiteAt() {
        Grid.Position p = new Grid.Position(14, 14);
        grid.setWhiteAt(p);
        assertEquals(grid.isWhiteAt(14,14), grid.isWhiteAt(p));
    }
}
    /*


    // -----------------------------
    // getCrossAt(int, int)
    // -----------------------------

    //crossAt
    @Test
    void getCrossAt_return_empty_on_new_grid() {
        assertEquals(Grid.CrossState.EMPTY, grid.getCrossAt(0, 0));
    }
    @Test
    void getCrossAt_return_Black_after_setBlackAt() {
        grid.setBlackAt(3, 3);
        assertEquals(Grid.CrossState.BLACK, grid.getCrossAt(3, 3));
    }
    @Test
    void getCrossAtWhite() {
        grid.setWhiteAt(9, 9);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(9, 9));
    }

    // getCrossAt(Position)
    @Test
    void getCrossAt_position_returns_value() {
        Grid.Position p = new Grid.Position(4, 4);
        grid.setWhiteAt(4, 4);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(p));
    }

    //seters blackAt e whiteAt






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
        //se non è vuota ed nera la riempiamo col nero
        if (!empty && occupyWithBlack) grid.setBlackAt(row, column);
        else
        //se non è vuota e non è nera la riempiamo col bianco
        if (!empty) grid.setWhiteAt(row, column);
        //oppure non la riempiamo completamente
        assertEquals(empty, grid.isEmpty(row, column));
    }


    @Test
    void occupyCrossWithBlack() {
        grid.setBlackAt(4, 4);
        assertEquals(Grid.CrossState.BLACK, grid.getCrossAt(4, 4));
    }


    @Test
    void occupyCrossWithWhite() {
        grid.setWhiteAt(4, 4);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(4, 4));
    }

    @Test
    void isBlackAt() {
        grid.setBlackAt(6, 6);
        assertEquals(true, grid.isBlackAt(6, 6));
    }

    @Test
    void isWhiteAt() {
        grid.setWhiteAt(8, 8);
        assertEquals(true, grid.isWhiteAt(8, 8));
    }


    @Test
    void notContainsPosition() {
        Grid.Position position = new Grid.Position(15, 15);
        assertEquals(false, grid.contains(position));
    }

    @Test
    void getCrossAtPosition() {
        Grid.Position position = new Grid.Position(4, 4);
        grid.setWhiteAt(4, 4);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(position));
    }
    @Test
    void getCrossWhiteAtPositionWithPosition() {
        Grid.Position position = new Grid.Position(4, 4);
        grid.setWhiteAt(position);
        assertEquals(Grid.CrossState.WHITE, grid.getCrossAt(position));
    }

    @Test
    void getCrossBlackAtPositionWithPosition() {
        Grid.Position position = new Grid.Position(7, 7);
        grid.setBlackAt(position);
        assertEquals(Grid.CrossState.BLACK, grid.getCrossAt(position));
    }

    @Test
    void getCrossAtPositionEmpty() {
        Grid.Position position = new Grid.Position(2, 2);
        assertEquals(Grid.CrossState.EMPTY, grid.getCrossAt(position));
    }


    */





