//valutare se cambiare nome in quanto non "conforme" alla programmazione a oggetti
public interface TurnBasedGame {
    // Comandi (Input)
    MoveResult makeMove(Position position);

    // Query (Output / Stato)
    //possibile leak di incapsulamento, valutare correzione
    //valutare se restituire un recordo o comunque una copia
    GameState getState();
    Player getCurrentPlayer();
    Grid getGrid();
}
