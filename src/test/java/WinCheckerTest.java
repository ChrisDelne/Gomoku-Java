import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WinCheckerTest {

    Grid grid = new Grid();
    WinChecker winChecker = new WinChecker(grid);

    @Test
    void isWinningMove_VerticalWin() {
        for (int r = 0; r < 5; r++) {
            grid.occupyCrossWithBlack(r, 10);
        }
        assertEquals(true, winChecker.isWinningMove(3, 10, Grid.Cross.BLACK));
    }

    @Test
    void isWinningMove_DiagonalWin() {
        for (int i = 0; i < 5; i++) {
            grid.occupyCrossWithBlack(i, i);
        }
        assertEquals(true, winChecker.isWinningMove(2, 2, Grid.Cross.BLACK));
    }

    @Test
    void isWinningMove_AntiDiagonalWin() {
        for (int i = 0; i < 5; i++) {
            grid.occupyCrossWithBlack(i, grid.getCOLUMNS()-1 - i);
        }
        assertEquals(true, winChecker.isWinningMove(2, 12, Grid.Cross.BLACK));
    }



    @Test
    void isWinningMove_HorizontalWin() {
        for (int c = 0; c < 5; c++) {
            grid.occupyCrossWithBlack(7, c);
        }
        assertEquals(true, winChecker.isWinningMove(7, 3, Grid.Cross.BLACK));
    }

   @Test
   void isWinningMove_NoWin() {
       grid.occupyCrossWithBlack(0, 0);
       grid.occupyCrossWithBlack(0, 1);
       grid.occupyCrossWithBlack(0, 2);
       grid.occupyCrossWithBlack(0, 3);
       assertEquals(false, winChecker.isWinningMove(0, 3, Grid.Cross.BLACK));
   }

   @Test
    void isWinningMove_noWinWithMixedColors() {
        grid.occupyCrossWithBlack(5, 5);
        grid.occupyCrossWithWhite(6, 6);
        grid.occupyCrossWithBlack(7, 7);
        grid.occupyCrossWithBlack(8, 8);
        grid.occupyCrossWithBlack(9, 9);
        assertEquals(false, winChecker.isWinningMove(9, 9, Grid.Cross.BLACK));
    }

    @Test
    void isWinningMove_WinWithMixedColors() {
        grid.occupyCrossWithBlack(10, 10);
        grid.occupyCrossWithBlack(11, 11);
        grid.occupyCrossWithWhite(12, 12);
        grid.occupyCrossWithBlack(13, 13);
        grid.occupyCrossWithBlack(14, 14);
        assertEquals(false, winChecker.isWinningMove(12, 12, Grid.Cross.WHITE));
    }

    @Test
    void isWinningMove_BoundaryWin() {
        for (int c = 10; c < 15; c++) {
            grid.occupyCrossWithBlack(14, c);
        }
        assertEquals(true, winChecker.isWinningMove(14, 12, Grid.Cross.BLACK));
    }

    @Test
    void isWinningMove_NoWinMultipleDirections() {
        grid.occupyCrossWithBlack(7, 7);
        grid.occupyCrossWithBlack(7, 8);
        grid.occupyCrossWithBlack(7, 9);
        grid.occupyCrossWithBlack(8, 7);
        grid.occupyCrossWithBlack(9, 7);
        grid.occupyCrossWithBlack(10, 7);
        assertEquals(false, winChecker.isWinningMove(7, 7, Grid.Cross.BLACK));
    }

}