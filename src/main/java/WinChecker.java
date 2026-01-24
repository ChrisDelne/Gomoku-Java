public class WinChecker {

    private final Grid grid;
    private static final int WIN_LENGHT = 5;

    public WinChecker(Grid grid) {
        this.grid = grid;
    }

    private int consecutiveCountFromNextPosition(Grid.Position position, int deltaRow, int deltaColumn, Grid.Cross startCrossColor) {
        int nextRow = position.row() + deltaRow;
        int nextColumn = position.col() + deltaColumn;
        int count = 0;

        while ( grid.contains(nextRow, nextColumn) && grid.getCrossAt(nextRow, nextColumn) == startCrossColor) {
            count++;
            nextRow += deltaRow;
            nextColumn += deltaColumn;
        }
        return count;
    }

    private int consecutiveCountBothDirections(Grid.Position position, Direction direction, Grid.Cross startCrossColor) {
        int deltaRow = direction.deltaRow();
        int deltaColumn = direction.deltaColumn();

        return 1 + consecutiveCountFromNextPosition(position, deltaRow, deltaColumn, startCrossColor)
                 + consecutiveCountFromNextPosition(position, -deltaRow, -deltaColumn, startCrossColor);
    }

    public boolean isWinningMove(Grid.Position position) {
        Grid.Cross startCrossColor = grid.getCrossAt(position);
        if (startCrossColor == Grid.Cross.EMPTY) {
            return false;
        } //se la croce di partenza è vuota non può essere una mossa vincente

        for (Direction direction : Direction.values()) {
            if (consecutiveCountBothDirections(position, direction, startCrossColor) >= WIN_LENGHT) {
                return true;
            }
        }
        return false;
    }

    public boolean isWinningMove(int row, int column) {
        return isWinningMove(new Grid.Position(row, column));
    }

}
