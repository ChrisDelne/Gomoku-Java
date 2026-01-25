public class Game {
    //seconeme me bisogna eliminare finished. ciri.
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

    //forse da levare perchè potrebbe infrangere il principio di encapsulation
    public Grid getGrid() {
        return grid;
    }


    //renderei lo scambio di turni una responsabilità del game e non di questo metodo
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

    public void endGameDraw(){
        //Succedono cose...
        state = GameState.DRAW;
    }

}
