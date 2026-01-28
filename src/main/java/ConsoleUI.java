import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;
    private final ConsoleRenderer renderer;


    // Accetta: "12 34", "12,34", "  -5   10  " ecc. (spazi e/o virgola e/o punto e virgola come separatore)
    private static final Pattern TWO_INTS = Pattern.compile("^\\s*([+-]?\\d+)\\s*[ ,;]+\\s*([+-]?\\d+)\\s*$");



    //--------------> Idee future: mostrare contatore turni, assegnare colore custom ai giocatori

    //passate, da eliminare
    //private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    //private static final String YELLOW = "\u001B[33m";
    //private static final String BLUE = "\u001B[34m";
    //private static final String PURPLE = "\u001B[35m";
    //private static final String CYAN = "\u001B[36m";
    //private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_WHITE = "\u001B[97m";
    //private static final String BRIGHT_BLACK = "\u001B[90m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String RESET  = "\u001B[0m";


    //valutare se spostare WinningPosition dentro il metodo Use e levarlo dalle variabili di classe
    private Set<Position> winningPositions = Set.of();

    public ConsoleUI(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
        this.renderer = new ConsoleRenderer(out);
    }

    private void render(TurnBasedGame game) {
        renderer.render(game.getGrid(), winningPositions);
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
        //da eliminare?
        winningPositions = Set.of(); // reset highlight all’inizio

        try {
            while (game.getState() == GameState.IN_PROGRESS) {
                render(game);
                handleMove(game);

                // Dopo una mossa valida, controlla se la partita è finita
                if (game.getState() != GameState.IN_PROGRESS) {
                    winningPositions = new HashSet<>(game.getDecisivePositions());
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




    //spostato
    private String colored(String color, String text) {
        return color + text + RESET;
    }



    private void handleMove(TurnBasedGame game) {
        while (true) {
            Player playerToMove = game.getCurrentPlayer();
            Position pos = readPosition("Giocatore "
                    + colored(playerToMove == Player.BLACK ? RED : GREEN,
                    playerToMove == Player.BLACK ? "NERO" : "BIANCO")
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

        // Se non è IN_PROGRESS e non è DRAW, allora qualcuno ha vinto
        //winningPositions = new HashSet<>(game.getDecisivePositions());

        Player winner = (state == GameState.BLACK_WON) ? Player.BLACK : Player.WHITE;
        String winnerName = (winner == Player.BLACK) ? "NERO" : "BIANCO";
        String winnerColor = (winner == Player.BLACK) ? RED : GREEN;

        out.println("Il gioco è terminato, vince: " + colored(winnerColor, winnerName) + "!");
    }


}