import java.util.Set;

public interface TurnBasedGame {
    // Comandi (Input)
    MoveResult makeMove(Position position);

    // Query (Output / Stato)
    //possibile leak di incapsulamento, valutare correzione
    //valutare se restituire un recordo o comunque una copia
    GameState getState();
    Player getCurrentPlayer();
    GridView getGrid();

    Set<Position> getDecisivePositions();
}
