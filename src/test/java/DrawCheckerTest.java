import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawCheckerTest {
    final Grid grid = new Grid();
    final DrawChecker drawChecker = new DrawChecker(grid);

    @Test
    void isDraw_FullGrid() {
        for (int r = 0; r < grid.getROWS(); r++)
            for (int c = 0; c < grid.getCOLUMNS(); c++)
                grid.setBlackAt(r, c);
        assertTrue(drawChecker.isDraw());
    }

    @Test
    void isDraw_NotFullGrid() {
        for (int r = 0; r < grid.getROWS(); r++)
            for (int c = 0; c < grid.getCOLUMNS(); c++) {
                if (r == grid.getROWS() - 1 && c == grid.getCOLUMNS() - 1)
                    continue; //Lascia una cella vuota
                if ((r + c) % 2 == 0)
                    grid.setBlackAt(r, c);
                else
                    grid.setWhiteAt(r, c);
            }
        assertFalse(drawChecker.isDraw());
    }

    @Test
    void isDraw_NotFullGridDraw() {
        for (int r = 0; r < grid.getROWS(); r++)
            for (int c = 0; c < grid.getCOLUMNS(); c++) {
                if (r == grid.getROWS() - 1 && c == grid.getCOLUMNS() - 1)
                    continue; //Lascia una cella vuota
                if (r == grid.getROWS() - 2 && c == grid.getCOLUMNS() - 2)
                    grid.setWhiteAt(r, c);
                else if ((r + c) % 2 == 0)
                    grid.setBlackAt(r, c);
                else
                    grid.setWhiteAt(r, c);
            }
        assertTrue(drawChecker.isDraw());
    }

}