import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUI {

    private final PrintStream out;
    private final ConsoleRenderer consoleRenderer;
    private final ConsoleInputReader consoleInputReader;


    public ConsoleUI(Scanner in, PrintStream out) {
        this.out = out;
        this.consoleRenderer = new ConsoleRenderer(out);
        this.consoleInputReader = new ConsoleInputReader(in, out);
    }

    private void render(TurnBasedGame game) {
        consoleRenderer.render(game.getGrid(), game.getDecisivePositions());
    }


    //gestire exception EOF
    public void use(TurnBasedGame game) {
        try {
            while (game.getState() == GameState.IN_PROGRESS) {
                render(game);
                handleMove(game);

                // Dopo una mossa valida, controlla se la partita è finita
                if (game.getState() != GameState.IN_PROGRESS) {
                    render(game); // ristampa la griglia finale
                    showEndMessage(game);
                    return;
                }
            }
            // creare un eccezione apposita per EOF perchè ora prende pure quella di handleMove
        } catch (InputTerminatedException e) {
            out.println("\nInput terminato. Uscita dalla partita.");
        } catch (IllegalStateException e) {
            out.println("\nErrore interno: " + e.getMessage());
            out.println("La partita verrà chiusa.");
        }
    }


    private void handleMove(TurnBasedGame game) {
        while (true) {
            Player playerToMove = game.getCurrentPlayer();
            Position pos = consoleInputReader.readPosition("Giocatore "
                    + consoleRenderer.playerLabel(playerToMove)
                    + " scrivi una posizione valida sulla griglia: ");

            MoveResult moveResult = game.makeMove(pos);

            if (moveResult.isValid())
                return;

            out.println("> " + moveResult.getReason());
        }
    }

    private void showEndMessage(TurnBasedGame game) {
        GameState state = game.getState();

        if (state == GameState.DRAW) {
            out.println("Il gioco è terminato: PARITÀ!");
            return;
        }

        Player winner = (state == GameState.BLACK_WON) ? Player.BLACK : Player.WHITE;
        out.println("Il gioco è terminato, vince: " + consoleRenderer.playerLabel(winner) + "!");
    }


}