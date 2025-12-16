public class Grid {
    public enum Cross {
        EMPTY,
        TAKEN
    }

    private final int ROWS = 15;
    private final int COLUMNS = 15;
    private final Cross[][] grid;

    public Grid() {
        grid = new Cross[ROWS][COLUMNS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                grid[r][c] = Cross.EMPTY;
            }
        }
    }

    public boolean isEmpty(int row, int column) {
        if (grid[row][column]==Cross.EMPTY) return true;
        else return false;
    }

    public void occupyCell(int row, int column) {
        grid[row][column] = Cross.TAKEN;
    }

}
