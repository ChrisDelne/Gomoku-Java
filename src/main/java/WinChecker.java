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


    private int consecutiveCount(Grid.Position position, Direction direction) {
        int count = 0;
        Grid.Cross startCrossColor = grid.getCrossAt(position);
        if (startCrossColor == Grid.Cross.EMPTY) {return count;} //ritorna 0 solo se la croce di partenza è vuota

        int row = position.row();
        int column = position.col();
        int deltaRow = direction.deltaRow();
        int deltaColumn = direction.deltaColumn();

        //fare due cicli diversi che partono da uno starting point e vanno in due direzioni opposte
        //finche sono dentro la griglia e il colore della croce di partenza è uguale a quello della croce corrente
        //la seconda condizione non è pericolosa java usa short-circuit evaluation e se siamo fuori dalla griglia non valuta la seconda condizione
        while ( grid.contains(row, column) && grid.getCrossAt(row, column) == startCrossColor) {
            count++;
            row += deltaRow;
            column += deltaColumn;
        }
        return count;
    }

    private int consecutiveCountOneDirections(Grid.Position position, int deltaRow, int deltaColumn) {
        int count = 0;
        Grid.Cross startCrossColor = grid.getCrossAt(position);
        if (startCrossColor == Grid.Cross.EMPTY) {return count;} //ritorna 0 solo se la croce di partenza è vuota

        int row = position.row();
        int column = position.col();

        //fare due cicli diversi che partono da uno starting point e vanno in due direzioni opposte
        //finche sono dentro la griglia e il colore della croce di partenza è uguale a quello della croce corrente
        //la seconda condizione non è pericolosa java usa short-circuit evaluation e se siamo fuori dalla griglia non valuta la seconda condizione
        while ( grid.contains(row, column) && grid.getCrossAt(row, column) == startCrossColor) {
            count++;
            row += deltaRow;
            column += deltaColumn;
        }
        return count;
    }

    private int consecutiveCountFromNextPosition(Grid.Position position, int deltaRow, int deltaColumn) {
        int nextRow = position.row() + deltaRow;
        int nextColumn = position.col() + deltaColumn;
        return consecutiveCountOneDirections(new Grid.Position(nextRow, nextColumn), deltaRow, deltaColumn);
    }

}
