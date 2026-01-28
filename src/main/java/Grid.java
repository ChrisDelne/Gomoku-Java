public class Grid implements GridView {

    private final int ROWS = 15;
    private final int COLUMNS = 15;
    private final CrossState[][] grid;

    public Grid() {
        grid = new CrossState[ROWS][COLUMNS];
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLUMNS; c++)
                grid[r][c] = CrossState.EMPTY;
    }

    public int getROWS() {
        return ROWS;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    public boolean contains(int row, int column) {
        return row >= 0 && row < ROWS && column >= 0 && column < COLUMNS;
    }

    public boolean contains(Position position) { //delega all'implementazione primaria
        return contains(position.row(), position.col());
    }

    //getters CrossState

    //Precedentemente getCrossAt
    public CrossState getStateAt(int row, int column) {
        return grid[row][column];
    }

    public CrossState getStateAt(Position position) {
        return getStateAt(position.row(), position.col());
    }


    public boolean isAt(CrossState cross, int row, int col) {
        return getStateAt(row, col) == cross;
    }

    public boolean isAt(CrossState cross, Position p) {
        return isAt(cross, p.row(), p.col());
    }

    public boolean isEmpty(int row, int column) {
        return isAt(CrossState.EMPTY, row, column);
    }

    public boolean isEmpty(Position position) { //delega all'implementazione primaria
        return isEmpty(position.row(), position.col());
    }

    //isBlackAt
    public boolean isBlackAt(int row, int column) {
        return isAt(CrossState.BLACK, row, column);
    }

    public boolean isBlackAt(Position position) { //delega all'implementazione primaria
        return isBlackAt(position.row(), position.col());
    }

    //isWhiteAt
    public boolean isWhiteAt(int row, int column) {
        return isAt(CrossState.WHITE, row, column);
    }

    public boolean isWhiteAt(Position position) { //delega all'implementazione primaria
        return isWhiteAt(position.row(), position.col());
    }


    //setters CrossState

    public void setBlackAt(int row, int column) { grid[row][column] = CrossState.BLACK; }

    public void setBlackAt(Position position) { //delega all'implementazione primaria
        setBlackAt(position.row(), position.col());
    }


    public void setWhiteAt(int row, int column) {
        grid[row][column] = CrossState.WHITE;
    }

    public void setWhiteAt(Position position) { //delega all'implementazione primaria
        setWhiteAt(position.row(), position.col());
    }

}
