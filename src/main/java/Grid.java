public class Grid {
    /*public enum CrossState {
        EMPTY,
        WHITE,
        BLACK
    }

    private final int ROWS = 15;
    private final int COLUMNS = 15;
    private final CrossState[][] grid;

    public Grid() {
        grid = new CrossState[ROWS][COLUMNS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                grid[r][c] = CrossState.EMPTY;
            }
        }
    }*/

    public static String returnCoordinatesOF(int x, int y) {
        return "(" + x + ";" + y + ")";
    }

    public static boolean isEmpty(int row, int column ) {
        if (row == 7 && column == 7) {
            return false;
        }
        if (row == 6 && column == 3) {
            return false;
        }
        if (row == 1 && column == 1) {
            return false;
        }
        return true;
    }

}
