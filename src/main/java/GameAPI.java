public interface GameAPI {
    // Comandi (Input)
    MoveResult makeMove(Grid.Position position);

    // Query (Output / Stato)
    GameState getState();
    Player getCurrentPlayer();
    Grid getGrid();
}
