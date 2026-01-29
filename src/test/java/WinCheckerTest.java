import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WinCheckerTest {

    private static final int WIN_LENGTH = 5;

    final Grid grid = new Grid();
    final WinChecker winChecker = new WinChecker(grid);

    private final int ROWS = grid.getRows();
    private final int COLS = grid.getColumns();
    private final int lastRow = ROWS - 1;
    private final int lastCol = COLS - 1;
    private final int midRow = ROWS / 2;
    private final int midCol = COLS / 2;

    @Test
    void getWinningLine_verticalWin() {
        // Setup
        for (int r = 0; r < WIN_LENGTH; r++)
            grid.setBlackAt(r, midCol);

        // Act
        List<Position> winningLine = winChecker.getWinningLine(new Position(WIN_LENGTH - 2, midCol));

        // Assert
        assertEquals(WIN_LENGTH, winningLine.size(), "Mi aspetto di trovare 5 pedine");
    }

    @Test
    void getWinningLine_diagonalWin() {
        for (int i = 0; i < WIN_LENGTH; i++)
            grid.setBlackAt(i, i);
        List<Position> winningLine = winChecker.getWinningLine(new Position(WIN_LENGTH / 2, WIN_LENGTH / 2));

        assertEquals(WIN_LENGTH, winningLine.size());
    }

    @Test
    void getWinningLine_antiDiagonalWin() {
        for (int i = 0; i < WIN_LENGTH; i++)
            grid.setBlackAt(i, lastCol - i);
        List<Position> winningLine = winChecker.getWinningLine(new Position(WIN_LENGTH / 2, lastCol - WIN_LENGTH / 2));

        assertEquals(WIN_LENGTH, winningLine.size());
    }

    @Test
    void getWinningLine_horizontalWin() {
        for (int c = 0; c < WIN_LENGTH; c++)
            grid.setBlackAt(midRow, c);
        List<Position> winningLine = winChecker.getWinningLine(new Position(midRow, WIN_LENGTH - 2));

        assertEquals(WIN_LENGTH, winningLine.size());
    }

    @Test
    void getWinningLine_noWin_whenShortLine() { // Solo 4 pedine
        for (int c = 0; c < WIN_LENGTH - 1; c++) {
            grid.setBlackAt(midRow, c);
        }
        List<Position> winningLine = winChecker.getWinningLine(new Position(midRow, WIN_LENGTH - 2));

        assertEquals(0, winningLine.size(), "Mi aspetto che nessuno vinca se meno di 5 pedine sono in fila ");
    }

    @Test
    void getWinningLine_noWin_whenMixedColors() {
        grid.setBlackAt(5, 5);
        grid.setWhiteAt(6, 6); // Interruzione
        grid.setBlackAt(7, 7);
        grid.setBlackAt(8, 8);
        grid.setBlackAt(9, 9);

        List<Position> resultBlack = winChecker.getWinningLine(new Position(9, 9));
        List<Position> resultWhite = winChecker.getWinningLine(new Position(6, 6));

        assertTrue(resultBlack.isEmpty() && resultWhite.isEmpty(), "Non deve vincere nessuno se i colori sono misti");
    }


    @Test
    void getWinningLine_winIgnoresInterruption_ifLineLongEnoughElsewhere() {
        for (int c = 0; c < WIN_LENGTH; c++) grid.setBlackAt(0, c); // Linea orizzontale valida
        grid.setWhiteAt(0, WIN_LENGTH);                             // Pedina avversaria subito dopo
        List<Position> result = winChecker.getWinningLine(new Position(0, 0));

        assertFalse(result.isEmpty());
    }

    @Test
    void getWinningLine_boundaryWin() { // Test bordo
        int startCol = COLS - WIN_LENGTH;
        for (int c = startCol; c < COLS; c++)
            grid.setBlackAt(lastRow, c);

        List<Position> result = winChecker.getWinningLine(new Position(lastRow, COLS - 1));

        assertFalse(result.isEmpty());
    }

    @Test
    void getWinningLine_noWin_ifScatteredStones() { // Pedine sparse che non formano una linea
        grid.setBlackAt(7, 7);
        grid.setBlackAt(7, 8);
        grid.setBlackAt(7, 9);
        grid.setBlackAt(6, 7);
        grid.setBlackAt(8, 7);
        grid.setBlackAt(9, 7);
        grid.setBlackAt(7, 6);

        List<Position> result = winChecker.getWinningLine(new Position(7, 7));

        assertTrue(result.isEmpty(), "5 pedine diverse \"in gruppo\" non devono vincere");
    }


    @Test
    void getWinningLine_findsAtLeastOne_ifMultipleDirections() {
        int row = 4;
        int col = 4;
        for (int i = 0; i < WIN_LENGTH; i++) {
            grid.setBlackAt(row, i); // Orizzontale
            grid.setBlackAt(i, col); // Verticale
        }

        // Deve ritornare una lista valida (una delle due)
        List<Position> result = winChecker.getWinningLine(new Position(row, col));

        assertTrue(result.size() >= 5);
    }

    @Test
    void getWinningLine_win_ifMoreThanFive() {
        int row = 6;
        int length = WIN_LENGTH + 2;
        for (int i = 0; i < length; i++)
            grid.setBlackAt(row, i);

        List<Position> result = winChecker.getWinningLine(new Position(row, WIN_LENGTH / 2));

        assertTrue(result.size() >= 5);
    }

    @Test
    void getWinningLine_emptyPositionReturnsEmptyLine() {
        List<Position> result = winChecker.getWinningLine(new Position(0, 0));
        assertTrue(result.isEmpty(), "Mi aspetto che una posizione vuota non dia vittoria");
    }

}