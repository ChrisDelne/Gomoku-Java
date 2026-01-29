import java.io.PrintStream;
import java.util.Set;

public class ConsoleRenderer {
    private static final String BLACK = "\u001B[31m"; //rosso
    private static final String WHITE = "\u001B[34m"; //blu
    private static final String POINTS = "\u001B[97m";
    private static final String HIGHLIGHTED_BACKGROUND = "\u001B[43m"; //gialo
    private static final String RESET = "\u001B[0m";

    private final PrintStream out;

    private int rows;
    private int cols;
    private int rowDigits;
    private int cellWidth;
    private int leftPad;
    private int rightPad;
    private int innerWidth;
    private String headerIndent;

    private void setupGridParams(GridView grid){
        rows = grid.getRows();
        cols = grid.getColumns();

        rowDigits = digits(rows - 1);
        final int colDigits = digits(cols - 1);

        // Ogni colonna “prenota”: (max cifre) + 1 spazio di separazione
        cellWidth = colDigits + 1;

        // Padding tra bordo e prima/ultima colonna
        leftPad = 1;
        rightPad = 1;

        /*  Larghezza interna tra ┌ e ┐:
                leftPad + (cols simboli) + (cols-1) * (spazi tra colonne) + rightPad
            es. 1 + 15 + 14 * 2 + 1
                spazio sx + numero puntini + spazio ai lati + spazio dx */
        innerWidth = leftPad + (cols - 1) * cellWidth + 1 + rightPad;

        /*  Dove inizia la colonna 0 (cioè dove sta il primo simbolo)
            Riga: [rowDigits cifre] + ' ' + '│' + leftPad spazi  => simbolo
            numero cifre + 2 spazi(separatore + colonna) + spazi leftpad */
        headerIndent = repeat(' ', rowDigits + 2) + repeat(' ', leftPad);
    }

    public ConsoleRenderer(PrintStream out) {
        this.out = out;
    }

    public void render(GridView g, Set<Position> positionsToHighlight) {
        clearScreenAndCursorToHome();
        printGameHeadlines();
        printGrid(g, positionsToHighlight);
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
        // math.max per evitare valori negativi
        return String.valueOf(ch).repeat(Math.max(0, count));
    }

    private String colored(String color, String text) {
        return color + text + RESET;
    }

    private String highlight(String text) {
        return HIGHLIGHTED_BACKGROUND + text + RESET;
    }

    private String symbol(Position p, CrossState state, Set<Position> winningPositions) {
        String base = switch (state) {
            case EMPTY -> colored(POINTS, "·");
            case BLACK -> colored(BLACK, "○");
            case WHITE -> colored(WHITE, "●");
        };

        if (winningPositions.contains(p))
            return highlight(base);

        return base;
    }

    private void printGrid(GridView grid, Set<Position> winningPositions) {
        setupGridParams(grid);
        printGridHeader();
        printGridRows(grid, winningPositions);
        printGridFooter();
    }

    private void printGameHeadlines() {
        out.println("===================== GOMOKU =====================\n");
        out.println("Player1: " + playerLabel(Player.BLACK) + "\t\tvs\t" + "   Player2: " + playerLabel(Player.WHITE) + "\n");
    }

    private void printGridHeader() {
        // ---------- Header colonne: prima cifra sopra la colonna ----------
        out.print(headerIndent);
        for (int c = 0; c < cols; c++) {
            String s = Integer.toString(c+1);       // Indici 0-based -> numerazione colonne
            out.print(s);                             // Stampa numerazione colonna
            out.print(repeat(' ', cellWidth - s.length())); // Riempi fino alla prossima colonna
        }
        out.println();

        // ---------- Bordo superiore ----------
        out.print(repeat(' ', rowDigits + 1));
        out.print('┌');
        out.print(repeat('─', innerWidth));
        out.print('┐');
        out.println();
    }

    private void printGridRows(GridView g, Set<Position> positionsToHighlight) {
        for (int r = 0; r < rows; r++) {
            out.printf("%" + rowDigits + "d ", r+1); // Indici 0-based -> numerazione righe

            // Padding sinistro: spazio dopo il bordo
            out.print('│');
            out.print(repeat(' ', leftPad));

            for (int c = 0; c < cols; c++) {
                out.print(symbol(new Position(r, c), g.getStateAt(r, c), positionsToHighlight));

                // Spazi SOLO tra colonne (non dopo l’ultima)
                if (c < cols - 1)
                    out.print(repeat(' ', cellWidth - 1));
            }

            // Padding destro: spazio prima del bordo
            out.print(repeat(' ', rightPad));
            out.print('│');
            out.println();
        }
    }

    private void printGridFooter() {
        out.print(repeat(' ', rowDigits + 1));
        out.print('└');
        out.print(repeat('─', innerWidth));
        out.print('┘');
        out.println();
    }

    public String playerLabel(Player p) {
        return (p == Player.BLACK) ? colored(BLACK, "NERO") : colored(WHITE, "BIANCO");
    }
}
