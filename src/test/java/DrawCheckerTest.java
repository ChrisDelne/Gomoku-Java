import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawCheckerTest {
    final Grid grid = new Grid();
    final DrawChecker drawChecker = new DrawChecker(grid);

    @Test
    void isDraw_notDraw_withFullGrid() {
        for (int r = 0; r < grid.getRows(); r++)
            for (int c = 0; c < grid.getColumns(); c++)
                grid.setBlackAt(r, c);
        assertTrue(drawChecker.isDraw());
    }

    @Test
    void isDraw_notDraw_withNotFullGrid() {
        for (int r = 0; r < grid.getRows(); r++)
            for (int c = 0; c < grid.getColumns(); c++) {

                boolean isLastCell = (r == grid.getRows() - 1 && c == grid.getColumns() - 1);
                boolean isBlack = ((r + c) % 2 == 0);

                if (!isLastCell) {
                    if (isBlack) grid.setBlackAt(r, c);
                    else grid.setWhiteAt(r, c);
                }
            }
        assertFalse(drawChecker.isDraw());
    }

    @Test
    void isDraw_draw_withNotFullGrid() {
        for (int r = 0; r < grid.getRows(); r++)
            for (int c = 0; c < grid.getColumns(); c++) {
                if (r == grid.getRows() - 1 && c == grid.getColumns() - 1)
                    continue; //Lascia una cella vuota
                if (r == grid.getRows() - 2 && c == grid.getColumns() - 2)
                    grid.setWhiteAt(r, c);
                else if ((r + c) % 2 == 0)
                    grid.setBlackAt(r, c);
                else
                    grid.setWhiteAt(r, c);
            }
        assertTrue(drawChecker.isDraw());
    }

}