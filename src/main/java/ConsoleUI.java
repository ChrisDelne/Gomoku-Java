import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner in;
    private final PrintStream out;


    public ConsoleUI(Scanner in, PrintStream out) {
        this.in = in;
        this.out = out;
    }


    public void play(Game game){}

    //Metodo tipico solo per capire funzionamento
    public void prova(){
        Game game = new Game();
        while(game.getState() == GameState.IN_PROGRESS) {
            Grid.Position p = askPosition();
            MoveResult result = game.makeMove(p);

            if(!result.isValid()) {
                System.out.println(result.reason());
            }
        }
    }

    //Metodo di prova
    private Grid.Position askPosition() {
        return null;
    }
}
