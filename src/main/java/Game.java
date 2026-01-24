public class Game {

    public enum GameState {
        NOT_STARTED,
        IN_PROGRESS,
        FINISHED
    }

    private Grid grid;
    private GameState state;
    private Player currentPlayer;
    private final MoveService moveService;

    public Game() {
        grid = new Grid();
        moveService = new MoveService(grid);
        state = GameState.IN_PROGRESS;
        currentPlayer = Player.BLACK;
    }

    public GameState getState() {
        return state;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Grid getGrid() {
        return grid;
    }

    public MoveResult registerMove(Grid.Position position) {
        if (state != GameState.IN_PROGRESS)
            throw new IllegalStateException("Game not in progress");

        MoveResult result = moveService.makeMove(currentPlayer, position);

        if (result == MoveResult.VALID_MOVE)
            switchTurn();

        return result;
    }

    private void switchTurn() {
        currentPlayer = currentPlayer.other();
    }

    public void endGame(){
        //Succedono cose...
        state = GameState.FINISHED;
    }

}
