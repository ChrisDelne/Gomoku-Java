public interface GridView {
    int getROWS();
    int getCOLUMNS();
    boolean contains(int row, int col);
    CrossState getStateAt(int row, int col);
    boolean contains(Position p);
    CrossState getStateAt(Position p);
}
