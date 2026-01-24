import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DrawCheckerTest {
    Grid grid = new Grid();
    DrawChecker drawChecker = new DrawChecker(grid);

    @Test
    void isDraw_FullGrid() {
        for (int r = 0; r < grid.getROWS(); r++) {
            for (int c = 0; c < grid.getCOLUMNS(); c++) {
                grid .occupyCrossWithBlack(r, c);
            }
        }
        assertEquals(true, drawChecker.isDraw());
    }

    @Test
    void isDraw_NotFullGrid() {
        for (int r = 0; r < grid.getROWS(); r++) {
            for (int c = 0; c < grid.getCOLUMNS(); c++) {
                if (r == grid.getROWS() - 1 && c == grid.getCOLUMNS() - 1) {
                    continue; // Leave one cell empty
                }
                if ((r + c) % 2 == 0) {
                    grid.occupyCrossWithBlack(r, c);
                } else {
                    grid.occupyCrossWithWhite(r, c);
                }
            }
        }
        assertEquals(false, drawChecker.isDraw());
    }

    @Test
    void isDraw_NotFullGridDraw() {
        for (int r = 0; r < grid.getROWS(); r++) {
            for (int c = 0; c < grid.getCOLUMNS(); c++) {
                if (r == grid.getROWS() - 1 && c == grid.getCOLUMNS() - 1) {
                    continue; // Leave one cell empty
                }
                if (r == grid.getROWS() - 2 && c == grid.getCOLUMNS() - 2) {
                    grid.occupyCrossWithWhite(r, c);
                } else if ((r + c) % 2 == 0) {
                    grid.occupyCrossWithBlack(r, c);
                } else {
                    grid.occupyCrossWithWhite(r, c);
                }
            }
        }
        assertEquals(true, drawChecker.isDraw());
    }

}