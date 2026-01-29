public interface GridView {
    int getRows();
    int getColumns();
    boolean contains(int row, int col);
    CrossState getStateAt(int row, int col);
    CrossState getStateAt(Position p);
    boolean isEmpty(Position position);
}
