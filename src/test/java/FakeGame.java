public class FakeGame implements GameAPI {

    // Campi fake per configurare il comportamento (Stub)
    private GameState stateToReturn = GameState.IN_PROGRESS;
    private Player playerToReturn = Player.BLACK;
    private MoveResult moveResultToReturn = MoveResult.VALID_MOVE;
    private Grid gridToReturn = new Grid();

    // Campi per verificare che la UI abbia chiamato i metodi giusti (Spy)
    public Grid.Position lastMovePositionReceived;
    public int makeMoveCallCount = 0;

    // Implementazione GameAPI
    @Override
    public MoveResult makeMove(Grid.Position position) {
        this.makeMoveCallCount++;
        this.lastMovePositionReceived = position;

        // Simula un cambio di stato o ritorna il risultato programmato
        return moveResultToReturn;
    }

    @Override
    public GameState getState() {
        return stateToReturn;
    }

    @Override
    public Player getCurrentPlayer() {
        return playerToReturn;
    }

    @Override
    public Grid getGrid() {
        return gridToReturn;
    }

    // Metodi setter per i Test
    public void setNextMoveResult(MoveResult result) {
        this.moveResultToReturn = result;
    }

    public void setState(GameState state) {
        this.stateToReturn = state;
    }

    public void setGrid(Grid grid) {
        this.gridToReturn = grid;
    }
}