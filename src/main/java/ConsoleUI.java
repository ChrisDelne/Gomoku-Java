import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;


    public ConsoleUI(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }


    public void use(GameAPI game){
        while (game.getState() == GameState.IN_PROGRESS) {
            continue;
        }
    }
}
