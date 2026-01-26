import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI(new Scanner(System.in), System.out);


        TurnBasedGame game = new Game();

        ui.use(game);
    }
}
