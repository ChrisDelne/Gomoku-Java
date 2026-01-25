public class Grid {

    //lo si puo rinominare coordinate o cross e rinominare cross in CrossState
    public record Position(int row, int col) {

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
    }

    public int getROWS() {
        return ROWS;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    //forse da sostituire a getROWS e getCOLUMNS
    /*
    public int getMaxRowIndex() {
        return ROWS - 1;
    }

    public int getMaxColumnIndex() {
        return COLUMNS - 1;
    }

    */

    // aggiungere funzione isInsideGrid isInsideBounds isInside (scegliere un nome)
    public boolean contains(int row, int column) {
        return row >= 0 && row < ROWS && column >= 0 && column < COLUMNS;
    }
    public boolean contains(Position position) {
        // delegate to primary implementation
        return contains(position.row(), position.col());
    }

    //getters CrossState

    //restituisce lo stato del cross, forse da rinominare in getCrossState
    public CrossState getCrossAt(int row, int column) {
        return grid[row][column];
    }
    public CrossState getCrossAt(Position position) {
        // delegate to primary implementation
        return getCrossAt(position.row(), position.col());
    }


    public boolean isAt(CrossState cross, int row, int col) {
        return getCrossAt(row, col) == cross;
    }
    public boolean isAt(CrossState cross, Position p) {
        return isAt(cross, p.row(), p.col());
    }

    public boolean isEmpty(int row, int column) {
        return isAt(CrossState.EMPTY , row, column);
    }
    public boolean isEmpty(Position position) {
        // delegate to primary implementation
        return isEmpty(position.row(), position.col());
    }

    //isBlackAt
    public boolean isBlackAt(int row, int column) {
        return isAt(CrossState.BLACK, row, column);
    }
    public boolean isBlackAt(Position position) {
        // delegate to primary implementation
        return isBlackAt(position.row(), position.col());
    }

    //isWhiteAt
    public boolean isWhiteAt(int row, int column) {
        return isAt(CrossState.WHITE, row, column);
    }
    public boolean isWhiteAt(Position position) {
        // delegate to primary implementation
        return isWhiteAt(position.row(), position.col());
    }



    //setters CrossState

    //possibile setAt(color, row, column))

    //placeBlackAt o SetBlackAt
    public void setBlackAt(int row, int column) {
        grid[row][column] = CrossState.BLACK;
    }
    public void setBlackAt(Position position) {
        // delegate to primary implementation
        setBlackAt(position.row(), position.col());
    }


    public void setWhiteAt(int row, int column) {
        grid[row][column] = CrossState.WHITE;
    }
    public void setWhiteAt(Position position) {
        // delegate to primary implementation
        setWhiteAt(position.row(), position.col());
    }

}
