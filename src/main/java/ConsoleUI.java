import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;
    private final ConsoleRenderer consoleRenderer;


    // Accetta: "12 34", "12,34", "  -5   10  " ecc. (spazi e/o virgola e/o punto e virgola come separatore)
    private static final Pattern TWO_INTS = Pattern.compile("^\\s*([+-]?\\d+)\\s*[ ,;]+\\s*([+-]?\\d+)\\s*$");


    public ConsoleUI(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
        this.consoleRenderer = new ConsoleRenderer(out);
    }

    private void render(TurnBasedGame game) {
        consoleRenderer.render(game.getGrid(), new HashSet<>(game.getDecisivePositions()));
    }


    //forse creare package per renderlo accessibile solo alla classe ed ai test che lo usano
    public Position readPosition(String prompt) {
        while (true) {                 // 1) ripeti finché non ottieni un input valido
            out.print(prompt);         // 2) mostra il messaggio all'utente

            if (!in.hasNextLine()) {   // 3) se non esiste un'altra riga (EOF, input chiuso)
                throw new InputTerminatedException("Input terminato.");
            }

            String line = in.nextLine();         // 4) leggi TUTTA la riga come stringa
            Matcher m = TWO_INTS.matcher(line);  // 5) prepara un "matcher" per confrontare la riga con la regex

            if (m.matches()) {         // 6) matches() = la riga intera rispetta la regex?
                try {
                    // 7) se sì, prendi i due pezzi catturati (group(1) e group(2))
                    int row = Integer.parseInt(m.group(1));
                    int col = Integer.parseInt(m.group(2));

                    // 8) ritorna la coppia -> esci dal metodo
                    //-1 converte da numero vero ad indice
                    return new Position(row, col);
                } catch (NumberFormatException ex) {
                    // caso: numero enorme fuori range di int
                    out.println("> Valore fuori range per int. Riprova.");
                }
            } else {
                out.println("> Input non valido: inserisci SOLO due numeri interi (es. \"3 4\").");
            }
        }
    }


    //gestire exception EOF
    //gestire numeri griglia != indici griglia
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
            Position pos = readPosition("Giocatore "
                    + consoleRenderer.playerLabel(playerToMove)
                    + " scrivi una posizione valida sulla griglia: ");

            MoveResult moveResult = game.makeMove(pos);

            if (moveResult.isValid()) {
                return;
            }

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