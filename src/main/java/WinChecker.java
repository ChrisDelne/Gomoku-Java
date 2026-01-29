import java.util.ArrayList;
import java.util.List;

public class WinChecker {

    private final GridView grid;
    private static final int WIN_LENGTH = 5;

    public WinChecker(GridView grid) {
        this.grid = grid;
    }

    private void addRun(List<Position> acc, int row, int col, int dRow, int dCol, CrossState color) {
        while (grid.contains(row, col) && grid.getStateAt(row, col) == color) {
            acc.add(new Position(row, col));
            row += dRow;
            col += dCol;
        }
    }

    // Aggiunge la posizione di partenza e poi scorre in entrambe le direzioni della linea
    private List<Position> collectLine(Position start, Direction dir, CrossState color) {

        List<Position> result = new ArrayList<>();
        result.add(start);

        int deltaRow = dir.deltaRow();
        int deltaCol = dir.deltaColumn();

        // avanti
        addRun(result, start.row() + deltaRow, start.col() + deltaCol, deltaRow, deltaCol, color);
        // indietro
        addRun(result, start.row() - deltaRow, start.col() - deltaCol, -deltaRow, -deltaCol, color);

        return result;
    }

    public List<Position> getWinningLine(Position position) { // Se mossa vincente, torna (calcola) a Game quali posizioni
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
