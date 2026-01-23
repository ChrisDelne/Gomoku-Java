public enum Direction {
    HORIZONTAL(0, 1),
    VERTICAL(1, 0),
    DIAGONAL_RIGHT(1, 1),
    DIAGONAL_LEFT(1, -1);

    private final int deltaRow;
    private final int deltaColumn;

    Direction(int deltaRow, int deltaColumn) {
        this.deltaRow = deltaRow;
        this.deltaColumn = deltaColumn;
    }

    public int deltaRow() {
        return deltaRow;
    }

    public int deltaColumn() {
        return deltaColumn;
    }
}
