import java.util.ArrayList;
import java.util.List;

public class WinChecker {

    private final Grid grid;
    private static final int WIN_LENGTH = 5;

    public WinChecker(Grid grid) {
        this.grid = grid;
    }

    //forse qua dentro il while si puo continuare ad usare position invece di dividerlo. però così bisogna scorrere creando un nuovo punto ogni volta
    private int consecutiveCountFromNextPosition(Position position, int deltaRow, int deltaColumn, CrossState startCrossColor) {
        int nextRow = position.row() + deltaRow;
        int nextColumn = position.col() + deltaColumn;
        int count = 0;

        while (grid.contains(nextRow, nextColumn) && grid.getStateAt(nextRow, nextColumn) == startCrossColor) {
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
        CrossState startCrossColor = grid.getStateAt(position);
        //Se l'incrocio di partenza è vuoto non può essere una mossa vincente
        if (startCrossColor == CrossState.EMPTY)
            return false;

        for (Direction direction : Direction.values())
            if (consecutiveCountBothDirections(position, direction, startCrossColor) >= WIN_LENGTH)
                return true;

        return false;
    }

    public boolean isWinningMove(int row, int column) {
        return isWinningMove(new Position(row, column));
    }

    private List<Position> collectLine(Position start, Direction dir, CrossState color) {

        List<Position> result = new ArrayList<>();
        result.add(start);

        int dr = dir.deltaRow();
        int dc = dir.deltaColumn();

        // avanti
        int r = start.row() + dr;
        int c = start.col() + dc;
        while (grid.contains(r, c) && grid.getStateAt(r, c) == color) {
            result.add(new Position(r, c));
            r += dr;
            c += dc;
        }

        // indietro
        r = start.row() - dr;
        c = start.col() - dc;
        while (grid.contains(r, c) && grid.getStateAt(r, c) == color) {
            result.add(new Position(r, c));
            r -= dr;
            c -= dc;
        }

        return result;
    }


    public List<Position> getWinningLine(Position position) {
        CrossState color = grid.getStateAt(position);
        if (color == CrossState.EMPTY)
            return List.of();

        for (Direction direction : Direction.values()) {
            List<Position> line = collectLine(position, direction, color);
            if (line.size() >= WIN_LENGTH)
                return line;
        }

        return List.of();
    }


}
