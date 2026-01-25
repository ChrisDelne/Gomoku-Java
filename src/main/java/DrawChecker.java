public class DrawChecker {
    private final Grid grid;

    public DrawChecker(Grid grid) {
        this.grid = grid;
    }

    private int countNoOpponentFromNextPosition(Grid.Position position, int deltaRow, int deltaColumn, CrossState opponentColor) {
        int nextRow = position.row() + deltaRow;
        int nextColumn = position.col() + deltaColumn;
        int count = 0;

        while ( grid.contains(nextRow, nextColumn) && grid.getCrossAt(nextRow, nextColumn) != opponentColor){
            count++;
            nextRow += deltaRow;
            nextColumn += deltaColumn;
        }
        return count;
    }

    private int countNoOpponentBothDirections(Grid.Position position, Direction direction, CrossState opponentColor) {
        int deltaRow = direction.deltaRow();
        int deltaColumn = direction.deltaColumn();

        return 1 + countNoOpponentFromNextPosition(position, deltaRow, deltaColumn, opponentColor)
                 + countNoOpponentFromNextPosition(position, -deltaRow, -deltaColumn, opponentColor);
    }

    private boolean canStillWin(CrossState player){
        CrossState opponent = (player == CrossState.BLACK) ? CrossState.WHITE : CrossState.BLACK;

        //scorriamo tutta la matrice
        for (int r = 0; r < grid.getROWS(); r++) {
            for (int c = 0; c < grid.getCOLUMNS(); c++) {
                Grid.Position position = new Grid.Position(r, c);
                //ci fermiamo quando troviamo una posizione vuota
                if (grid.isEmpty(position)) {
                    //testiamo se da questa posizione c'è una finestra vincente per il giocatore
                    for (Direction direction : Direction.values()) {
                        if (countNoOpponentBothDirections(position, direction, opponent) >= 5) {
                            return true;
                        }
                    }
                }
            }
        }
    return false;
    }

    public boolean isDraw(){
        return !(canStillWin(CrossState.BLACK) || canStillWin(CrossState.WHITE));
    }

}
