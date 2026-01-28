public interface GridView {
    int getROWS();
    int getCOLUMNS();
    boolean contains(int row, int col);
    CrossState getStateAt(int row, int col);
    default boolean contains(Position p) { return contains(p.row(), p.col()); }
    default CrossState getStateAt(Position p) { return getStateAt(p.row(), p.col()); }
}
