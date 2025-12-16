import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridCoordinatesTest {

    @ParameterizedTest
    @CsvSource({
        "3,3,(3;3)",
        "13,1,(13;1)",
        "11,15,(11;15)"
    })
    public void crossVariousCoordinates(int x, int y, String expected) {
        assertEquals(expected, Grid.returnCoordinatesOF(x, y));
    }
}
