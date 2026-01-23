public class WinChecker {

    Grid grid;

    public WinChecker(Grid grid) {
        this.grid = grid;
    }


    private int consecutiveCount(int row, int column, int deltaRow, int deltaColumn) {
        int count = 0;
        Grid.Cross startCrossColor = grid.getCrossAt(row, column);
        //finche sono dentro la griglia e il colore della croce di partenza non è vuoto (forse si può togliere questa seconda condizione dal ciclo e farla prima come if)
        while ( grid.contains(row, column) && startCrossColor != Grid.Cross.EMPTY) {
            if (grid.getCrossAt(row, column) == startCrossColor) {
                count++;
                row += deltaRow;
                column += deltaColumn;
            } else {
                break;
            }
        }
        return count;
    }

    public boolean isWinningMove(int row, int column) {
        int[][] directions = {
                {0, 1},   // Horizontal
                {1, 0},   // Vertical
                {1, 1},   // Diagonal \
                {1, -1}   // Diagonal /
        };

        for (int[] direction : directions) {
            int deltaRow = direction[0];
            int deltaColumn = direction[1];

            int count = -1;  // Start with -1 to avoid double counting the starting position

            // Count in the positive direction
            count += consecutiveCount(row, column, deltaRow, deltaColumn);
            // Count in the negative direction
            count += consecutiveCount(row, column, -deltaRow, -deltaColumn);
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }
    //equivalente a isWinningMove ma usa l'enum Direction, forse l'enum potrebbe essere passato direttamente a consecutiveCount
    public boolean isWinningMove2(int row, int column){
        for (Direction direction : Direction.values()) {
            int deltaRow = direction.deltaRow();
            int deltaColumn = direction.deltaColumn();

            int count = -1;  // Start with -1 to avoid double counting the starting position

            // Count in the positive direction
            count += consecutiveCount(row, column, deltaRow, deltaColumn);
            // Count in the negative direction
            count += consecutiveCount(row, column, -deltaRow, -deltaColumn);
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

}
