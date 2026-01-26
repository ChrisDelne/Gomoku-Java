import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;

    // Accetta: "12 34", "12,34", "  -5   10  " ecc. (spazi e/o virgola come separatore)
    private static final Pattern TWO_INTS = Pattern.compile("^\\s*([+-]?\\d+)\\s*[ ,;]+\\s*([+-]?\\d+)\\s*$");

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
                    out.println("Valore fuori range per int. Riprova.");
                }
            } else {
                out.println("Input non valido: inserisci SOLO due numeri interi (es. \"3 4\").");
            }
        }
    }



    //gestire exception EOF
    //gestire numeri griglia != indici griglia

    public void use(TurnBasedGame game){
        while (game.getState() == GameState.IN_PROGRESS) {
            clearScreen();
            printGrid(game.getGrid());

            try {
                MoveResult moveResult = game.makeMove(readPosition("scrivi una posizione valida sulla griglia: "));
                while (!moveResult.isValid()) {
                    moveResult = game.makeMove(readPosition("scrivi una nuova posizione valida sulla griglia: "));
                }
            }catch (IllegalStateException eof){
                out.println("\nInput terminato. Uscita dalla partita.");
                return; //esce
            }

        }
    }

    private void clearScreen() {
        // ANSI: clear entire screen + cursor to home
        out.print("\u001B[H\u001B[2J");
        out.flush();
    }

    private void printGrid(Grid g) {
        final int rows = g.getROWS();
        final int cols = g.getCOLUMNS();

        // Ogni cella è stampata come: "<simbolo><spazio>", tranne l'ultima senza spazio finale.
        // Quindi larghezza contenuto = 2*cols - 1
        final int contentWidth = 2 * cols - 1;

        // Header colonne (allineato all'inizio delle celle)
        out.print("     "); // 5 spazi: allinea con "rr │ " (2 + 1 + 1 + 1)
        for (int c = 0; c < cols; c++) {
            if (c < 10)out.print(c+" "); // 0..9: una cifra
            else out.print(c);                    // 10..14: due cifre (qui va gestito lo spazio sotto)
            if (c != cols - 1) out.print(' ');
        }
        out.println();

        // Bordo superiore
        out.print("   "); // 3 spazi (allinea sotto il header)
        out.print('┌');
        for (int i = 0; i < contentWidth + 2; i++) out.print('─'); // +2 per gli spazi interni "│ <cells> │"
        out.print('┐');
        out.println();

        // Righe
        for (int r = 0; r < rows; r++) {
            // Etichetta riga (2 caratteri minimi per allineare 0..14)
            out.printf("%2d ", r);

            out.print('│');
            out.print(' ');

            for (int c = 0; c < cols; c++) {
                out.print(symbol(g.getCrossAt(r, c)));
                if (c != cols - 1) out.print(' ');
            }

            out.print(' ');
            out.print('│');
            out.println();
        }

        // Bordo inferiore
        out.print("   ");
        out.print('└');
        for (int i = 0; i < contentWidth + 2; i++) out.print('─');
        out.print('┘');
        out.println();
    }

    private char symbol(CrossState cs) {
        return switch (cs) {
            case EMPTY -> '·';
            case BLACK -> '●';
            case WHITE -> '○';
        };
    }





}
