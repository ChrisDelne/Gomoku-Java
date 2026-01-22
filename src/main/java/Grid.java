public class Grid {
    public enum Cross {
        EMPTY,
        TAKEN,
        BLACK,
        WHITE
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
        return grid[row][column] == Cross.EMPTY;
    }

    public boolean isOccupiedByBlack(int row, int column) {
        return grid[row][column] == Cross.BLACK;
    }

    public boolean isOccupiedByWhite(int row, int column) {
        return grid[row][column] == Cross.WHITE;
    }

    public void occupyCrossWithBlack(int row, int column) {
        grid[row][column] = Cross.BLACK;
    }

    public void occupyCrossWithWhite(int row, int column) {
        grid[row][column] = Cross.WHITE;
    }

}
