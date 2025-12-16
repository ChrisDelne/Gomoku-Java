import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    @ParameterizedTest
    @CsvSource({
        "3,3,(3;3)",
        "13,1,(13;1)",
        "11,15,(11;15)"
    })
    public void printCoordinates(int x, int y, String expected) {
        assertEquals(expected, Grid.returnCoordinatesOF(x, y));
    }

    @Test
    public void empty5x5() {
        assertEquals(true, Grid.isEmpty(5, 5));
    }

    @Test
    public void empty10x15() {
        assertEquals(true, Grid.isEmpty(10, 15));
    }

    @Test
    public void empty1x5() {
        assertEquals(true, Grid.isEmpty(1, 5));
    }

    @Test
    public void notEmpty7x7() {
        assertEquals(false, Grid.isEmpty(7, 7));
    }

    @Test
    public void notEmpty6x3() {
        assertEquals(false, Grid.isEmpty(6, 3));
    }

    @Test
    public void notEmpty1x1() {
        assertEquals(false, Grid.isEmpty(1, 1));
    }

}
