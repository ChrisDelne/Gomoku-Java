public class Game {

    private Pawn playerB;
    private Pawn playerW;
    private Grid grid;

    public Game() {
        initializeGame();
    }

    private void initializeGame() {
        grid = new Grid();
        playerB = new Pawn(Pawn.Color.BLACK);
        playerW = new Pawn(Pawn.Color.WHITE);
    }

    public Grid getGrid() {
        return grid;
    }

    public Pawn getPlayerBlack() {
        return playerB;
    }

    public Pawn getPlayerWhite() {
        return playerW;
    }

}
