import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GridTest {

    private final Grid grid = new Grid();
    final int maxIndexRow = grid.getRows() - 1;
    final int maxIndexCol = grid.getColumns() - 1;


    // ---------------- griglia inizializzata vuota ------------------------

    @Test
    void newGrid_isAllEmpty() {
        boolean allEmpty =
                IntStream.range(0, grid.getRows()).allMatch(r ->
                        IntStream.range(0, grid.getColumns()).allMatch(c ->
                                grid.getStateAt(r, c) == CrossState.EMPTY));

        assertTrue(allEmpty);
    }

    // ----------------- contains -------------------------

    @ParameterizedTest
    @CsvSource({
            "1,1,false",
            "-1,1,false",
            "1,-1,false",
            "0,0,true"
    })
    void contains_intInt(int x, int y, boolean expected) {
        assertEquals(expected, grid.contains(maxIndexRow + x, maxIndexCol + y));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,14",
            "-1,30",
            "0,-1",
            "15,0",
            "0,15"
    })
    void containsPosition_equalsContainsIntInt(int row, int col) {
        Position p = new Position(row, col);

        assertEquals(grid.contains(row, col), grid.contains(p));
    }

    // ----------------- lettura/scrittura stato ------------------------

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,3",
            "4,5",
    })
    void setBlackAtIntInt_readableByGetStateAt(int x, int y) {
        grid.setBlackAt(maxIndexRow - x, maxIndexCol - y);

        assertEquals(CrossState.BLACK, grid.getStateAt(maxIndexRow - x, maxIndexCol - y));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "3,3",
            "5,1",
    })
    void setWhiteAtIntInt_readableByGetStateAt(int row, int col) {
        grid.setWhiteAt(row, col);

        assertEquals(CrossState.WHITE, grid.getStateAt(row, col));
    }

    // ------ isAt / isEmpty / isBlackAt / isWhiteAt (int,int) ------

    @ParameterizedTest
    @CsvSource({
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
    void isAt_allCombinations(String actual, String requested, boolean expected) {
        int r = 2, c = 5; // check sullo stesso punto con condizioni diverse
        CrossState actualState = CrossState.valueOf(actual);
        CrossState requestedState = CrossState.valueOf(requested);

        if (actualState == CrossState.BLACK) grid.setBlackAt(r, c);
        if (actualState == CrossState.WHITE) grid.setWhiteAt(r, c);
        // EMPTY -> non fare nulla

        assertEquals(expected, grid.isAt(requestedState, r, c));
    }

    @Test
    void isEmpty_false_afterSetBlackAt() {
        grid.setBlackAt(1, 1);

        assertFalse(grid.isEmpty(1, 1));
    }

    @Test
    void isBlackAt_true_afterSetBlackAt() {
        grid.setBlackAt(7, 7);

        assertTrue(grid.isBlackAt(7, 7));
    }

    @Test
    void isBlackAt_false_whenCellIsWhite() {
        grid.setWhiteAt(2, 5);

        assertFalse(grid.isBlackAt(2, 5));
    }

    @Test
    void isWhiteAt_true_afterSetWhiteAt() {
        grid.setWhiteAt(8, 8);

        assertTrue(grid.isWhiteAt(8, 8));
    }

    @Test
    void isWhiteAt_false_whenCellIsBlack() {
        grid.setBlackAt(2, 5);

        assertFalse(grid.isWhiteAt(2, 5));
    }

    // ------ Overload Position: getStateAt(Position) ------

    @Test
    void getStateAtPosition_equalsGetStateAtIntInt() {
        Position p = new Position(2, 2);
        grid.setBlackAt(2, 2);

        assertEquals(grid.getStateAt(2, 2), grid.getStateAt(p));
    }

    // ------ Overload Position: setter ------

    @ParameterizedTest
    @CsvSource({
            "0,3",
            "1,0",
            "3,5",
    })
    void setBlackAtPosition_writesBlack(int x, int y) {
        Position p = new Position(maxIndexRow - x, maxIndexCol - y);
        grid.setBlackAt(p);

        assertEquals(CrossState.BLACK, grid.getStateAt(maxIndexRow - x, maxIndexCol - y));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,1",
            "4,5",
    })
    void setWhiteAtPosition_writesWhite(int x, int y) {
        Position p = new Position(maxIndexRow - x, maxIndexCol - y);
        grid.setWhiteAt(p);

        assertEquals(CrossState.WHITE, grid.getStateAt(maxIndexRow - x, maxIndexCol - y));
    }

    // ------ Overload Position: isAt / isEmpty / isBlackAt / isWhiteAt ------

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,1",
            "4,5",
    })
    void isAtPosition_true_whenStateMatches(int x, int y) {
        Position p = new Position(maxIndexRow - x, maxIndexCol - y);
        grid.setWhiteAt(p);

        assertTrue(grid.isAt(CrossState.WHITE, p));
    }

    @Test
    void isEmptyPosition_true_onNewGrid() {
        Position p = new Position(0, 0);

        assertEquals(grid.isEmpty(0, 0), grid.isEmpty(p));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0",
            "1,3",
            "4,5",
    })
    void isBlackAtPosition_true_afterSetBlackAt(int row, int col) {
        Position p = new Position(row, col);
        grid.setBlackAt(p);

        assertEquals(grid.isBlackAt(row, col), grid.isBlackAt(p));
    }

    @ParameterizedTest
    @CsvSource({
            "0,1",
            "3,3",
            "6,5",
    })
    void isWhiteAtPosition_true_afterSetWhiteAt(int row, int col) {
        Position p = new Position(row, col);
        grid.setWhiteAt(p);

        assertEquals(grid.isWhiteAt(row, col), grid.isWhiteAt(p));
    }
}