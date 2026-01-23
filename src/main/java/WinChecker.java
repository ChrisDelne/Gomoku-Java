public class WinChecker {

    Grid grid;

    public WinChecker(Grid grid) {
        this.grid = grid;
    }


    private int consecutiveCount(int row, int column, int deltaRow, int deltaColumn, Grid.Cross player) {
        int count = 0;
        while (row >= 0 && row < grid.getROWS()  && column >= 0 && column < grid.getCOLUMNS()) {
            if ((player == Grid.Cross.BLACK && grid.isOccupiedByBlack(row, column)) ||
                (player == Grid.Cross.WHITE && grid.isOccupiedByWhite(row, column))) {
                count++;
                row += deltaRow;
                column += deltaColumn;
            } else {
                break;
            }
        }
        return count;
    }

    public boolean isWinningMove(int row, int column, Grid.Cross player) {
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
            count += consecutiveCount(row, column, deltaRow, deltaColumn, player);
            // Count in the negative direction
            count += consecutiveCount(row, column, -deltaRow, -deltaColumn, player);
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

}
