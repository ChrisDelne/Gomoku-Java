import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WinCheckerTest {

    Grid grid = new Grid();
    WinChecker winChecker = new WinChecker(grid);

    @Test
    void getWinningLine_VerticalWin() {
        // Setup
        for (int r = 0; r < 5; r++)
            grid.setBlackAt(r, 10);

        // Act
        List<Position> winningLine = winChecker.getWinningLine(new Position(3, 10));

        // Assert
        assertEquals(5, winningLine.size(), "Mi aspetto di trovare 5 pedine");
    }

    @Test
    void getWinningLine_DiagonalWin() {
        for (int i = 0; i < 5; i++)
            grid.setBlackAt(i, i);
        List<Position> winningLine = winChecker.getWinningLine(new Position(2, 2));
        assertEquals(5, winningLine.size());
    }

    @Test
    void getWinningLine_AntiDiagonalWin() {
        for (int i = 0; i < 5; i++)
            grid.setBlackAt(i, 14 - i);
        List<Position> winningLine = winChecker.getWinningLine(new Position(2, 12));
        assertEquals(5, winningLine.size());
    }

    @Test
    void getWinningLine_HorizontalWin() {
        for (int c = 0; c < 5; c++)
            grid.setBlackAt(7, c);
        List<Position> winningLine = winChecker.getWinningLine(new Position(7, 3));
        assertEquals(5, winningLine.size());
    }

    @Test
    void getWinningLine_NoWin_ShortLine() { // Solo 4 pedine
        grid.setBlackAt(0, 0);
        grid.setBlackAt(0, 1);
        grid.setBlackAt(0, 2);
        grid.setBlackAt(0, 3);

        List<Position> winningLine = winChecker.getWinningLine(new Position(0, 3));
        assertEquals(0, winningLine.size(), "Mi aspetto che nessuno vinca se meno di 5 pedine sono in fila ");
    }

    @Test
    void getWinningLine_NoWin_MixedColors() {
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
    void getWinningLine_Win_IgnoresInterruption_IfLineIsLongEnoughElsewhere() {
        for (int c = 0; c < 5; c++) grid.setBlackAt(0, c); // Linea orizzontale valida
        grid.setWhiteAt(0, 5); // Pedina avversaria subito dopo

        List<Position> result = winChecker.getWinningLine(new Position(0, 0));
        assertFalse(result.isEmpty());
    }

    @Test
    void getWinningLine_BoundaryWin() { // Test bordo
        for (int c = 10; c < 15; c++)
            grid.setBlackAt(14, c);
        List<Position> result = winChecker.getWinningLine(new Position(14, 12));
        assertFalse(result.isEmpty());
    }

    @Test
    void getWinningLine_NoWin_ScatteredStones() { // Pedine sparse che non formano una linea
        grid.setBlackAt(7, 7);
        grid.setBlackAt(7, 8);
        grid.setBlackAt(7, 9);
        grid.setBlackAt(6, 7);
        grid.setBlackAt(8, 7);
        grid.setBlackAt(9, 7);
        grid.setBlackAt(7, 6);

        List<Position> result = winChecker.getWinningLine(new Position(7, 7));
        assertTrue(result.isEmpty(), "5 pedine \"in gruppo\" non devono vincere");
    }

    @Test
    void getWinningLine_MultipleDirections_FindsAtLeastOne() {
        for (int i = 0; i < 5; i++) {
            grid.setBlackAt(5, i); // Orizzontale
            grid.setBlackAt(i, 5); // Verticale
        }

        // Deve ritornare una lista valida (una delle due)
        List<Position> result = winChecker.getWinningLine(new Position(5, 2));
        assertFalse(result.isEmpty());
        assertTrue(result.size() >= 5);
    }

    @Test
    void getWinningLine_Overline_MoreThanFive() { // Almeno 5 in fila
        for (int i = 0; i < 7; i++)
            grid.setBlackAt(6, i);

        List<Position> result = winChecker.getWinningLine(new Position(6, 3));
        assertTrue(result.size() >= 5);
    }

    @Test
    void getWinningLine_EmptyPositionReturnsEmptyLine() {
        List<Position> result = winChecker.getWinningLine(new Position(0, 0));
        assertTrue(result.isEmpty(), "Mi aspetto che una posizione vuota non dia vittoria");
    }

}