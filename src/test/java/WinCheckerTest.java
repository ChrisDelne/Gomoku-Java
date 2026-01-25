import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WinCheckerTest {

    Grid grid = new Grid();
    WinChecker winChecker = new WinChecker(grid);

    @Test
    void isWinningMove_VerticalWin() {
        for (int r = 0; r < 5; r++) {
            grid.setBlackAt(r, 10);
        }
        assertEquals(true, winChecker.isWinningMove(3, 10));
    }

    @Test
    void isWinningMove_DiagonalWin() {
        for (int i = 0; i < 5; i++) {
            grid.setBlackAt(i, i);
        }
        assertEquals(true, winChecker.isWinningMove(2, 2));
    }

    @Test
    void isWinningMove_AntiDiagonalWin() {
        for (int i = 0; i < 5; i++) {
            grid.setBlackAt(i, grid.getCOLUMNS()-1 - i);
        }
        assertEquals(true, winChecker.isWinningMove(2, 12));
    }



    @Test
    void isWinningMove_HorizontalWin() {
        for (int c = 0; c < 5; c++) {
            grid.setBlackAt(7, c);
        }
        assertEquals(true, winChecker.isWinningMove(7, 3));
    }

   @Test
   void isWinningMove_NoWin() {
       grid.setBlackAt(0, 0);
       grid.setBlackAt(0, 1);
       grid.setBlackAt(0, 2);
       grid.setBlackAt(0, 3);
       assertEquals(false, winChecker.isWinningMove(0, 3));
   }

   @Test
    void isWinningMove_noWinWithMixedColors() {
        grid.setBlackAt(5, 5);
        grid.setWhiteAt(6, 6);
        grid.setBlackAt(7, 7);
        grid.setBlackAt(8, 8);
        grid.setBlackAt(9, 9);
        assertEquals(false, winChecker.isWinningMove(9, 9));
    }

    @Test
    void isWinningMove_WinWithMixedColors() {
        grid.setBlackAt(10, 10);
        grid.setBlackAt(11, 11);
        grid.setWhiteAt(12, 12);
        grid.setBlackAt(13, 13);
        grid.setBlackAt(14, 14);
        assertEquals(false, winChecker.isWinningMove(12, 12));
    }

    @Test
    void isWinningMove_BoundaryWin() {
        for (int c = 10; c < 15; c++) {
            grid.setBlackAt(14, c);
        }
        assertEquals(true, winChecker.isWinningMove(14, 12));
    }

    @Test
    void isWinningMove_NoWinMultipleDirections() {
        grid.setBlackAt(7, 7);
        grid.setBlackAt(7, 8);
        grid.setBlackAt(7, 9);
        grid.setBlackAt(8, 7);
        grid.setBlackAt(9, 7);
        grid.setBlackAt(10, 7);
        assertEquals(false, winChecker.isWinningMove(7, 7));
    }

    @Test
    void isWinningMove_WinMultipleDirections() {
        for (int i = 0; i < 5; i++) {
            grid.setBlackAt(5, i); // Horizontal
            grid.setBlackAt(i, 5); // Vertical
        }
        assertEquals(true, winChecker.isWinningMove(5, 2));
    }

    @Test
    void isWinningMove_PointIsNotOccupied() {
        assertEquals(false, winChecker.isWinningMove(0, 0));
    }

}