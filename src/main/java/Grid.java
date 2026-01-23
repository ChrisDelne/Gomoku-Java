public class Grid {


    public enum Cross {
        EMPTY,
        BLACK,
        WHITE
    }

    //lo si puo rinominare coordinate o cross e rinominare cross in CrossState
    public record Position(int row, int col) {

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

    //restituisce lo stato del cross, forse da rinominare in getCrossState o getCrossValue
    public Cross getCrossAt(int row, int column) {
        return grid[row][column];
    }
    public Cross getCrossAt(Position position) {
        return grid[position.row][position.col];
    }



    // aggiungere funzione isInsideGrid isInsideBounds isInside (scegliere un nome)
    public boolean contains(int row, int column) {
        return row >= 0 && row < ROWS && column >= 0 && column < COLUMNS;
    }
    public boolean contains(Position position) {
        return position.row >= 0 && position.row < ROWS && position.col >= 0 && position.col < COLUMNS;
    }


    public boolean isEmpty(int row, int column) {
        return grid[row][column] == Cross.EMPTY;
    }
    public boolean isEmpty(Position position) {
        return grid[position.row][position.col] == Cross.EMPTY;
    }


    public boolean isOccupiedByBlack(int row, int column) {
        return grid[row][column] == Cross.BLACK;
    }
    public boolean isOccupiedByBlack(Position position) {
        return grid[position.row][position.col] == Cross.BLACK;
    }


    public boolean isOccupiedByWhite(int row, int column) {
        return grid[row][column] == Cross.WHITE;
    }
    public boolean isOccupiedByWhite(Position position) {
        return grid[position.row][position.col] == Cross.WHITE;
    }


    public void occupyCrossWithBlack(int row, int column) {
        grid[row][column] = Cross.BLACK;
    }
    public void occupyCrossWithBlack(Position position) {
        grid[position.row][position.col] = Cross.BLACK;
    }


    public void occupyCrossWithWhite(int row, int column) {
        grid[row][column] = Cross.WHITE;
    }
    public void occupyCrossWithWhite(Position position) {
        grid[position.row][position.col] = Cross.WHITE;
    }

}
