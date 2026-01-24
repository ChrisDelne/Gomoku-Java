public class Game {

    private Grid grid;

    public Game() {
        initializeGame();
    }

    private void initializeGame() {
        grid = new Grid();
    }

    public Grid getGrid() {
        return grid;
    }

}
