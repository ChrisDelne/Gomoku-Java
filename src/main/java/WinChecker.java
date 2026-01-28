import java.util.ArrayList;
import java.util.List;

public class WinChecker {

    private final Grid grid;
    private static final int WIN_LENGTH = 5;

    public WinChecker(Grid grid) {
        this.grid = grid;
    }

    //Aggiunge la posizione di partenza e poi scorre in entrambe le direzioni della linea
    private List<Position> collectLine(Position start, Direction dir, CrossState color) {

        List<Position> result = new ArrayList<>();
        result.add(start);

        int deltaRow = dir.deltaRow();
        int deltaCol = dir.deltaColumn();

        // avanti per la stessa direzione
        int row = start.row() + deltaRow;
        int col = start.col() + deltaCol;
        while (grid.contains(row, col) && grid.getStateAt(row, col) == color) {
            result.add(new Position(row, col));
            row += deltaRow;
            col += deltaCol;
        }

        // indietro per la stessa direzione
        row = start.row() - deltaRow;
        col = start.col() - deltaCol;
        while (grid.contains(row, col) && grid.getStateAt(row, col) == color) {
            result.add(new Position(row, col));
            row -= deltaRow;
            col -= deltaCol;
        }

        return result;
    }


    public List<Position> getWinningLine(Position position) { //Se mossa vincente, torna (calcola) a Game quali posizioni
        CrossState color = grid.getStateAt(position);
        if (color == CrossState.EMPTY)
            return List.of();

        for (Direction direction : Direction.values()) { // per tutte le direzioni
            List<Position> line = collectLine(position, direction, color); // controlla avanti-indietro
            if (line.size() >= WIN_LENGTH)
                return line;
        }

        return List.of();
    }


}
