import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

public class FakeGame implements TurnBasedGame {

    // ---------------- Stub behavior -------------------------
    private GameState state = GameState.IN_PROGRESS;
    private final Grid grid = new Grid();
    private final Deque<MoveResult> scriptedMoveResults = new ArrayDeque<>();


    // --------------- Stateful: cambio stato dopo N mosse -------------------------
    private int changeStateAfterMoves = 0; // 0 = disabilitato
    private GameState stateAfterChange = null;


    // --------------- Spy: dati registrati ---------------------
    private int makeMoveCallCount = 0;


    // --------------- Implementazione TurnBasedGame -------------------------

    @Override
    public MoveResult makeMove(Position positionNotUsed) {
        makeMoveCallCount++;

        // Se configurato, cambia lo stato dopo N chiamate (utile per far terminare un loop UI)
        // controlliamo sia impostato ad un valore valido (non -1) e facciamo il confronto
        if (changeStateAfterMoves > 0 && makeMoveCallCount >= changeStateAfterMoves)
            if (stateAfterChange != null)
                state = stateAfterChange;

        // Ritorna il prossimo risultato scriptato, altrimenti un default sensato
        if (!scriptedMoveResults.isEmpty())
            return scriptedMoveResults.removeFirst();
        return MoveResult.VALID_MOVE;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public Player getCurrentPlayer() {
        return Player.BLACK;
    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public Set<Position> getDecisivePositions() {
        return Set.of();
    }


    // ----------------- Metodi di configurazione (fluent) usati dai test ----------------------

    public FakeGame withState(GameState state) {
        this.state = state;
        return this;
    }

    /*  Script dei risultati che makeMove() ritornerà in sequenza.
        es. withScriptedMoveResults(INVALID_MOVE, VALID_MOVE) */
    public FakeGame withScriptedMoveResults(MoveResult... results) {
        scriptedMoveResults.clear();
        for (MoveResult r : results)
            scriptedMoveResults.addLast(r);
        return this;
    }

    /*  Dopo 'moves' chiamate a makeMove() imposta lo stato a 'newState'
        Utile per simulare la fine del gioco e interrompere la UI */
    public FakeGame endGameAfterMoves(int moves, GameState newState) {
        this.changeStateAfterMoves = moves;
        this.stateAfterChange = newState;
        return this;
    }

    // ----------------- Getter spy (per assert nei test) -------------------------

    public int getMakeMoveCallCount() {
        return makeMoveCallCount;
    }

}