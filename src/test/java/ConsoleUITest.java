import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ConsoleUITest {


    //prima vanno implementati i test di lettura

    // legge piu input validi
    @Test
    void Console_can_read_2_int_from_input() {
        Scanner in = new Scanner(new ByteArrayInputStream("10 20\n".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);

        ConsoleUI ui = new ConsoleUI(in, out);

        assertEquals(new Position(10, 20), ui.readPosition(""));
    }

    //scarta gli imput non validi
    @ParameterizedTest
    @CsvSource({
            "'abc\n10 20',          10,20",
            "'abc\n10,a\n10 20',    10,20",
            "'adf\nsdg\nfgj\n3 4',  3,4",
            "';\n,\nciao\n7 8',     7,8"
    })
    void readPosition_skips_any_number_of_invalid_inputs(String inputLines, int expectedRow, int expectedCol) {
        Scanner in = new Scanner(new ByteArrayInputStream((inputLines + "\n").getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8);
        ConsoleUI ui = new ConsoleUI(in, out);

        Position pos = ui.readPosition("");
        assertEquals(new Position(expectedRow, expectedCol), pos);
    }


    //lancia eccezione EOF
    @Test
    void readPosition_throws_if_input_ends() {
        Scanner in = new Scanner(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        ConsoleUI ui = new ConsoleUI(in, out);

        assertThrows(IllegalStateException.class, () -> ui.readPosition(""));
    }




    //ConsoleUI consoleUI = new ConsoleUI(new Scanner(System.in), System.out);
    @Disabled
    @Test
    void in_progres_make_move(){
        FakeGame game = new FakeGame()
                .withState(GameState.IN_PROGRESS);


        String userInput = "1 2\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        // da valutare
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);

        // Act
        consoleUI.use(game);

        // Assert: nessuna mossa applicata
        assertEquals(1, game.getMakeMoveCallCount(),
                "Se lo stato è IN_PROGRESS, ConsoleUI deve chiamare makeMove().");


    }



    //se lo stato non è in progress non applica la mossa
    @Disabled
    @Test
    void not_in_progres_no_move(){
        FakeGame game = new FakeGame()
                .withState(GameState.BLACK_WON);

        // Input "finto": anche se l'utente scrivesse una mossa, la UI non dovrebbe leggerla
        String userInput = "1 2\n";
        Scanner in = new Scanner(new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8)));

        // Output catturato (opzionale, utile se vuoi controllare cosa stampa)
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);

        ConsoleUI consoleUI = new ConsoleUI(in, out);

        // Act
        consoleUI.use(game);

        // Assert: nessuna mossa applicata
        assertEquals(0, game.getMakeMoveCallCount(),
                "Se lo stato non è IN_PROGRESS, ConsoleUI non deve chiamare makeMove().");


    }


}
