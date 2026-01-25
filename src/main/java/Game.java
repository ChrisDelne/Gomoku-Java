public class Game {

    public enum GameState {
        //FINISHED,
        IN_PROGRESS,
        BLACK_WON,
        WHITE_WON,
        DRAW
    }

    private final Grid grid;
    private GameState state;
    private Player currentPlayer;
    private final MoveService moveService;
    private final DrawChecker drawChecker;
    private final WinChecker winChecker;



    public Game() {
        grid = new Grid();
        moveService = new MoveService(grid);
        drawChecker = new DrawChecker(grid);
        winChecker = new WinChecker(grid);
        state = GameState.IN_PROGRESS;
        currentPlayer = Player.BLACK;
    }


    //getter
    public GameState getState() {
        return state;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Grid getGrid() {
        return grid;
    }


    //renderei lo scambio di turni una responsabilità del game e non di questo metodo
    public MoveResult makeMove(Grid.Position position) {
        if (state != GameState.IN_PROGRESS)
            throw new IllegalStateException("Game not in progress");

        MoveResult result = moveService.registerMove(currentPlayer, position);
        if (result.isValid())
            advanceGameAfterValidMove(position);

        return result;
    }


    private void advanceGameAfterValidMove(Grid.Position position) {
        if (winChecker.isWinningMove(position)) { // vittoria
            state = (currentPlayer == Player.BLACK) ? GameState.BLACK_WON : GameState.WHITE_WON;
            return;
        }

        if (drawChecker.isDraw()) // pareggio
            state = GameState.DRAW;

        if (state == GameState.IN_PROGRESS) // running
            switchTurn();
    }


    private void switchTurn() {
        currentPlayer = currentPlayer.other();
    }

}
