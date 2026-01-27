import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;

    // Accetta: "12 34", "12,34", "  -5   10  " ecc. (spazi e/o virgola come separatore)
    private static final Pattern TWO_INTS = Pattern.compile("^\\s*([+-]?\\d+)\\s*[ ,;]+\\s*([+-]?\\d+)\\s*$");

    //--------------> Idee future: mostrare contatore turni, assegnare colore custom ai giocatori
    private static final boolean USE_COLORS = true; //Interruttore globale dei colori

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

    private Set<Position> winningPositions = Set.of();

    public ConsoleUI(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }


    //forse creare package per renderlo accessibile solo alla classe ed ai test che lo usano
    public Position readPosition(String prompt) {
        while (true) {                 // 1) ripeti finché non ottieni un input valido
            out.print(prompt);         // 2) mostra il messaggio all'utente

            if (!in.hasNextLine()) {   // 3) se non esiste un'altra riga (EOF, input chiuso)
                throw new IllegalStateException("Input terminato.");
            }

            String line = in.nextLine();         // 4) leggi TUTTA la riga come stringa
            Matcher m = TWO_INTS.matcher(line);  // 5) prepara un "matcher" per confrontare la riga con la regex

            if (m.matches()) {         // 6) matches() = la riga intera rispetta la regex?
                try {
                    // 7) se sì, prendi i due pezzi catturati (group(1) e group(2))
                    int x = Integer.parseInt(m.group(1));
                    int y = Integer.parseInt(m.group(2));

                    // 8) ritorna la coppia -> esci dal metodo
                    //-1 converte da numero vero ad indice
                    return new Position(x, y);
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
        while (game.getState() == GameState.IN_PROGRESS) {
            clearScreenAndCursorToHome();
            printGrid(game.getGrid());

            try {
                MoveResult moveResult;
                Player currentPlayer = game.getCurrentPlayer();
                do {
                    Position pos = readPosition("Giocatore "
                            + colored(currentPlayer == Player.BLACK ? RED : GREEN, currentPlayer == Player.BLACK ? "NERO" : "BIANCO")
                            + " scrivi una posizione valida sulla griglia: ");
                    moveResult = game.makeMove(pos);

                    GameState gameState = game.getState();
                    if (!moveResult.isValid()) {
                        out.println("> " + moveResult.getReason());
                    } else if(gameState != GameState.IN_PROGRESS && gameState != GameState.DRAW) {
                        winningPositions = new HashSet<>(game.getDecisivePositions());
                        clearScreenAndCursorToHome();
                        printGrid(game.getGrid());
                        out.println("Il gioco è terminato, vince: "
                                + colored(currentPlayer == Player.BLACK ? RED : GREEN, gameState == GameState.BLACK_WON ? "NERO" : "BIANCO")
                                + "!");
                    } else if(gameState == GameState.DRAW) {
                        clearScreenAndCursorToHome();
                        printGrid(game.getGrid());
                        out.println("Il gioco è terminato: PARITÀ!");
                    }
                } while (!moveResult.isValid());
            } catch (IllegalStateException eof) {
                out.println("\nInput terminato. Uscita dalla partita.");
                return; //esce
            }

        }
    }

    private void clearScreenAndCursorToHome() {
        out.print("\u001B[H\u001B[2J");
        out.flush();
    }

    private String colored(String color, String text) {
        return USE_COLORS ? color + text + RESET : text;
    }

    private String highlight(String text) {
        return USE_COLORS ? BG_YELLOW + text + RESET : text;
    }

    private String symbol(Position p, CrossState state) {
        String base = switch (state) {
            case EMPTY -> colored(BRIGHT_WHITE, "\u00B7"); // ·
            case BLACK -> colored(RED, "\u25CF"); // ●
            case WHITE -> colored(GREEN, "\u25CB"); // ○
        };

        if (winningPositions.contains(p))
            return highlight(base);

        return base;
    }


    private void printGrid(Grid g) {
        final int rows = g.getROWS();
        final int cols = g.getCOLUMNS();

        out.println("===================== GOMOKU =====================\n");
        out.println("Player1: " + colored(RED, "NERO") + "\t\tvs\t" + "   Player2: " + colored(GREEN, "BIANCO") + "\n");

        // Ogni cella è "<simbolo><spazio>" tranne l'ultima senza spazio -> larghezza contenuto = 2*cols - 1
        final int contentWidth = 2 * cols - 1;

        // Header colonne (allineato all'inizio delle celle)
        out.print("     ");
        for (int c = 0; c < cols; c++) {
            if (c < 10) out.print(c + " ");
            else out.print(c);
            if (c != cols - 1) out.print(' ');
        }
        out.println();

        // Bordo superiore
        out.print("   ");
        out.print('┌');
        for (int i = 0; i < contentWidth + 16; i++) out.print('─');
        out.print('┐');
        out.println();

        // Righe
        for (int r = 0; r < rows; r++) {
            // Etichetta riga (2 caratteri minimi per allineare 0..14)
            out.printf("%2d ", r);

            out.print('│');
            out.print(' ');

            for (int c = 0; c < cols; c++) {
                out.print(symbol(new Position(r,c), g.getStateAt(r,c)) + " ");
                if (c != cols - 1) out.print(' ');
            }

            out.print('│');
            out.println();
        }

        // Bordo inferiore
        out.print("   ");
        out.print('└');
        for (int i = 0; i < contentWidth + 16; i++) out.print('─');
        out.print('┘');
        out.println();
    }

}
