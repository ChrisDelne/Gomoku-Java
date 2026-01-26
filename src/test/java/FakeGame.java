import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FakeGame implements GameAPI {


    // -------------------------
    // Stato "query" che la UI legge
    // -------------------------
    // Campi fake per configurare il comportamento (Stub)
    private GameState state = GameState.IN_PROGRESS;
    private Player currentPlayer = Player.BLACK;
    private Grid grid = new Grid();


    // --- Script for makeMove results (Stub behaviour) --
    private final Deque<MoveResult> scriptedMoveResults = new ArrayDeque<>();


    // -------------------------
    // Stateful: cambio stato dopo N mosse
    // -------------------------
    private int changeStateAfterMoves = -1; // -1 = disabilitato
    private GameState stateAfterChange = null;


    // -------------------------
    // Spy: dati registrati
    // -------------------------
    private int makeMoveCallCount = 0;
    //farla diventare una lista delle mosse effettivamente attuate oppure crearne una seconda
    private final List<Position> receivedPositions = new ArrayList<>();


    // =========================================================
    // Implementazione GameAPI
    // =========================================================


    @Override
    public MoveResult makeMove(Position position) {
        makeMoveCallCount++;
        receivedPositions.add(position);

        // Se configurato, cambia lo stato dopo N chiamate (utile per far terminare un loop UI)
        //controlliamo sia impostato ad un valore valido(non -1) e nel caso facciamo il confronto
        if (changeStateAfterMoves >= 0 && makeMoveCallCount >= changeStateAfterMoves) {
            if (stateAfterChange != null) {
                state = stateAfterChange;
            }
        }

        // Ritorna il prossimo risultato scriptato, altrimenti un default sensato
        if (!scriptedMoveResults.isEmpty()) {
            return scriptedMoveResults.removeFirst();
        }
        return MoveResult.VALID_MOVE;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Grid getGrid() {
        return grid;
    }



    // =========================================================
    // Metodi di configurazione (fluent) usati dai test
    // =========================================================

    /** Imposta lo stato ritornato da getState(). */
    public FakeGame withState(GameState state) {
        this.state = state;
        return this;
    }

    /** Imposta il player ritornato da getCurrentPlayer(). */
    public FakeGame withCurrentPlayer(Player player) {
        this.currentPlayer = player;
        return this;
    }

    /** Imposta la griglia ritornata da getGrid(). */
    public FakeGame withGrid(Grid grid) {
        this.grid = grid;
        return this;
    }


    /*
    Script dei risultati che makeMove() ritornerà in sequenza.
    Esempio: withScriptedMoveResults(INVALID_MOVE, VALID_MOVE)
    */
    public FakeGame withScriptedMoveResults(MoveResult... results) {
        scriptedMoveResults.clear();
        for (MoveResult r : results) {
            scriptedMoveResults.addLast(r);
        }
        return this;
    }

    /**
     * Dopo 'moves' chiamate a makeMove(), imposta lo stato a 'newState'.
     * Utile per simulare la fine del gioco e interrompere la UI.
     */
    public FakeGame endGameAfterMoves(int moves, GameState newState) {
        this.changeStateAfterMoves = moves;
        this.stateAfterChange = newState;
        return this;
    }

    //non so se effettivamente serve
    /** (Opzionale) Resetta solo i dati spy (contatori e posizioni). */
    public FakeGame resetSpy() {
        this.makeMoveCallCount = 0;
        this.receivedPositions.clear();
        return this;
    }


    // =========================================================
    // Getter spy (per assert nei test)
    // =========================================================

    public int getMakeMoveCallCount() {
        return makeMoveCallCount;
    }

    /** Ritorna una copia delle posizioni ricevute, in ordine. */
    public List<Position> getReceivedPositions() {
        return new ArrayList<>(receivedPositions);
    }

    /** Ultima posizione ricevuta, o null se nessuna. */
    public Position getLastReceivedPosition() {
        if (receivedPositions.isEmpty()) return null;
        return receivedPositions.getLast();
    }

}