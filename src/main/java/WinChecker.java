public class WinChecker {

    private final Grid grid;
    private static final int WIN_LENGHT = 5;

    public WinChecker(Grid grid) {
        this.grid = grid;
    }

    //forse qua dentro il while si puo continuare ad usare position invece di dividerlo. però così bisogna scorrere creando un nuovo punto ogni volta
    private int consecutiveCountFromNextPosition(Position position, int deltaRow, int deltaColumn, CrossState startCrossColor) {
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

    private int consecutiveCountBothDirections(Position position, Direction direction, CrossState startCrossColor) {
        int deltaRow = direction.deltaRow();
        int deltaColumn = direction.deltaColumn();

        return 1 + consecutiveCountFromNextPosition(position, deltaRow, deltaColumn, startCrossColor)
                 + consecutiveCountFromNextPosition(position, -deltaRow, -deltaColumn, startCrossColor);
    }

    public boolean isWinningMove(Position position) {
        CrossState startCrossColor = grid.getCrossAt(position);
        if (startCrossColor == CrossState.EMPTY) {
            return false;
        } //se la croce di partenza è vuota non può essere una mossa vincente

        for (Direction direction : Direction.values()) {
            if (consecutiveCountBothDirections(position, direction, startCrossColor) >= WIN_LENGHT) {//per richiedere 5 preciso disattivare il test Win_Multiple_Directions_more_of_five
                return true;
            }
        }
        return false;
    }

    public boolean isWinningMove(int row, int column) {
        return isWinningMove(new Position(row, column));
    }

}
