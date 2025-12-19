import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PawnTest {

    @Test
    void pawnHasColor() {
        Pawn pawn = new Pawn(Pawn.Color.BLACK);
        assertEquals(Pawn.Color.BLACK, pawn.getColor());
    }
}
