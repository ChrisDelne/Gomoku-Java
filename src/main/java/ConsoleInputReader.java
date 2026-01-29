import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleInputReader {

    private final Scanner scanner;
    private final PrintStream out;


    // Accetta: "12 34", "12,34", "  -5   10  " ecc. (spazi e/o virgola e/o punto e virgola come separatore)
    private static final Pattern TWO_INTS = Pattern.compile("^\\s*([+-]?\\d+)\\s*[ ,;]+\\s*([+-]?\\d+)\\s*$");


    public ConsoleInputReader(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    /**
     * Reads a valid position from the user.
     * This method is now rightfully PUBLIC because it is the primary service of this class.
     */
    public Position readPosition(String prompt) {
        while (true) {
            out.print(prompt);

            if (!scanner.hasNextLine())
                throw new InputTerminatedException("Input terminato.");

            String line = scanner.nextLine();
            Matcher m = TWO_INTS.matcher(line);

            if (m.matches()) {
                try {
                    int row = Integer.parseInt(m.group(1));
                    int col = Integer.parseInt(m.group(2));

                    // Logic for 1-based to 0-based conversion remains here
                    return new Position(row - 1, col - 1);
                } catch (NumberFormatException ex) {
                    out.println("> Valore fuori range per int. Riprova.");
                }
            } else {
                out.println("> Input non valido: inserisci SOLO due numeri interi (es. \"3 4\").");
            }
        }
    }
}