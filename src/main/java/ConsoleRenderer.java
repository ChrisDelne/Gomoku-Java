import java.io.PrintStream;
import java.util.Set;

public class ConsoleRenderer {
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

    private final PrintStream out;

    public ConsoleRenderer(PrintStream out) {
        this.out = out;
    }

    public void render(Grid g, Set<Position> winningPositions) {
        clearScreenAndCursorToHome();
        printGrid(g, winningPositions);
    }

    private void clearScreenAndCursorToHome() {
        out.print("\u001B[H\u001B[2J");
        out.flush();
    }


    private static int digits(int n) {
        // n >= 0
        return String.valueOf(n).length();
    }

    private static String repeat(char ch, int count) {
        //math.max per evitare valori negativi
        return String.valueOf(ch).repeat(Math.max(0, count));
    }

    private String colored(String color, String text) {
        return color + text + RESET;
    }

    private String highlight(String text) {
        return BG_YELLOW + text + RESET;
    }

    private String symbol(Position p, CrossState state, Set<Position> winningPositions) {
        String base = switch (state) {
            case EMPTY -> colored(BRIGHT_WHITE, "\u00B7"); // ·
            case BLACK -> colored(RED, "\u25CF"); // ●
            case WHITE -> colored(GREEN, "\u25CB"); // ○
        };

        if (winningPositions.contains(p))
            return highlight(base);

        return base;
    }

    private void printGrid(Grid g, Set<Position> winningPositions) {
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
                out.print(symbol(new Position(r, c), g.getStateAt(r, c), winningPositions));

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

    //da valutare
    private String playerLabel(Player p) {
        return (p == Player.BLACK) ? colored(RED, "NERO") : colored(GREEN, "BIANCO");
    }
}
