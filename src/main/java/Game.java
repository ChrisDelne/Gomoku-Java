import java.util.List;

public class Game implements TurnBasedGame {

    private final Grid grid;
    private GameState state;
    private Player currentPlayer;
    private final MoveService moveService;
    private final DrawChecker drawChecker;
    private final WinChecker winChecker;
    private List<Position> winningLine = List.of(); // Lista posizioni vincenti vuota se nessuno ha vinto



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

    public List<Position> getDecisivePositions() { return winningLine; }

    @Override
    public MoveResult makeMove(Position position) {
        if (state != GameState.IN_PROGRESS)
            throw new IllegalStateException("Game not in progress");

        MoveResult result = moveService.registerMove(currentPlayer, position);
        if (result.isValid())
            advanceGameAfterValidMove(position);

        return result;
    }


    private void advanceGameAfterValidMove(Position position) {
        List<Position> line = winChecker.getWinningLine(position);
        if (!line.isEmpty()) { // vittoria se la lista delle posizioni vincenti non è vuota
            winningLine = line; // game sa ora quali sono le posizioni vincenti (date alla UI con getter)
            state = (currentPlayer == Player.BLACK) ? GameState.BLACK_WON : GameState.WHITE_WON;
            return;
        }

        /*if (winChecker.isWinningMove(position)) { // vittoria
            state = (currentPlayer == Player.BLACK) ? GameState.BLACK_WON : GameState.WHITE_WON;
            return;
        }*/

        if (drawChecker.isDraw()) { // pareggio
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
