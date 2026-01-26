public class Game implements TurnBasedGame {

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

    //FORSE seperarerei makeMove, advanceGameAfterValidMove e switchTurn che attualmente sono metodi annidati
    //il primo fa pure gli altri tre, non è chiaro dal nome
    //e dividerli aumenta l'indipendenza tra le responsabilità
    //renderei lo scambio di turni una responsabilità della ConsoleUI e non di questo metodo
    public MoveResult makeMove(Position position) {
        if (state != GameState.IN_PROGRESS)
            throw new IllegalStateException("Game not in progress");

        MoveResult result = moveService.registerMove(currentPlayer, position);
        if (result.isValid())
            advanceGameAfterValidMove(position);

        return result;
    }


    private void advanceGameAfterValidMove(Position position) {
        if (winChecker.isWinningMove(position)) { // vittoria
            state = (currentPlayer == Player.BLACK) ? GameState.BLACK_WON : GameState.WHITE_WON;
            return;
        }

        if (drawChecker.isDraw()){
            // pareggio
            state = GameState.DRAW;
            return;
        }

        if (state == GameState.IN_PROGRESS) // running
            switchTurn();
    }


    private void switchTurn() {
        currentPlayer = currentPlayer.other();
    }

}
