import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;

    // Accetta: "12 34", "12,34", "  -5   10  " ecc. (spazi e/o virgola e/o punto e virgola come separatore)
    private static final Pattern TWO_INTS = Pattern.compile("^\\s*([+-]?\\d+)\\s*[ ,;]+\\s*([+-]?\\d+)\\s*$");



    //--------------> Idee future: mostrare contatore turni, assegnare colore custom ai giocatori

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
        winningPositions = Set.of(); // reset highlight all’inizio

        try {
            while (game.getState() == GameState.IN_PROGRESS) {
                render(game);

                Player playerToMove = game.getCurrentPlayer();
                handleMove(game, playerToMove);

                // Dopo una mossa valida, controlla se la partita è finita
                if (game.getState() != GameState.IN_PROGRESS) {
                    winningPositions = new HashSet<>(game.getDecisivePositions());
                    render(game); // ristampa la griglia finale
                    showEndMessage(game);
                    return;
                }
            }
        } catch (IllegalStateException eof) {
            out.println("\nInput terminato. Uscita dalla partita.");
        }
    }

    private void clearScreenAndCursorToHome() {
        out.print("\u001B[H\u001B[2J");
        out.flush();
    }

    private String colored(String color, String text) {
        return color + text + RESET;
    }

    private String highlight(String text) {
        return BG_YELLOW + text + RESET;
    }

    //qui viene messo 2 volte  reset in caso di hilights, una in colored e una in highlight
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


        //leavare meno uno se si passa a numerazione nomrale invece che index
        final int rowDigits = digits(rows - 1);
        final int colDigits = digits(cols - 1);

        // Ogni colonna “prenota”: (max cifre) + 1 spazio di separazione
        final int cellWidth = colDigits + 1;

        // padding interno tra bordo e prima/ultima colonna
        final int leftPad = 1;
        final int rightPad = 1;

        // Larghezza interna tra ┌ e ┐:
        // leftPad +
        // (cols simboli) +
        // (cols-1) * (spazi tra colonne) +
        // rightPad
        // es. 1 + 15 + 14 * 2 + 1
        //   spazio s. + numero puntini + spazio ai lati + spazio destra
        final int innerWidth = leftPad + (cols - 1) * cellWidth + 1 + rightPad;

        // Dove inizia la colonna 0 (cioè dove sta il primo simbolo)
        // Riga: [rowDigits cifre] + ' ' + '│' + leftPad spazi  => simbolo
        // numero cifre + 2 spazi(separatore + colonna) + spazi leftpad
        final String headerIndent = repeat(' ', rowDigits + 2) + repeat(' ', leftPad);

        // ---------- Header colonne: prima cifra sopra la colonna ----------
        out.print(headerIndent);
        for (int c = 0; c < cols; c++) {
            String s = Integer.toString(c);                 // "0", "9", "10", ...
            out.print(s);                                   // prima cifra va “a colonna”
            out.print(repeat(' ', cellWidth - s.length())); // riempi fino alla prossima colonna
        }
        out.println();

        // ---------- Bordo superiore ----------
        out.print(repeat(' ', rowDigits + 1));
        out.print('┌');
        out.print(repeat('─', innerWidth));
        out.print('┐');
        out.println();

        // ---------- Righe ----------
        for (int r = 0; r < rows; r++) {
            out.printf("%" + rowDigits + "d ", r);

            out.print('│');
            out.print(repeat(' ', leftPad));

            for (int c = 0; c < cols; c++) {
                out.print(symbol(new Position(r, c), g.getStateAt(r, c)));

                // spazi SOLO tra colonne (non dopo l’ultima)
                if (c < cols - 1) {
                    out.print(repeat(' ', cellWidth - 1));
                }
            }

            // padding destro “giusto”: 1 spazio prima del bordo
            out.print(repeat(' ', rightPad));
            out.print('│');
            out.println();
        }

        // ---------- Bordo inferiore ----------
        out.print(repeat(' ', rowDigits + 1));
        out.print('└');
        out.print(repeat('─', innerWidth));
        out.print('┘');
        out.println();
    }


    private static int digits(int n) {
        // n >= 0
        return String.valueOf(n).length();
    }

    private static String repeat(char ch, int count) {
        //math.max per evitare valori negativi
        return String.valueOf(ch).repeat(Math.max(0, count));
    }

    private void render(TurnBasedGame game) {
        clearScreenAndCursorToHome();
        printGrid(game.getGrid());
    }

    private void handleMove(TurnBasedGame game, Player playerToMove) {
        while (true) {
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