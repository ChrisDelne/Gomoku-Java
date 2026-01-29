import java.util.Set;

public interface TurnBasedGame {

    MoveResult makeMove(Position position);

    GameState getState();

    Player getCurrentPlayer();

    GridView getGrid();

    Set<Position> getDecisivePositions();

}
