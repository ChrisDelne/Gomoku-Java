import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameTest {

    @Test
    void gameInitialization_createsGridAndPlayers() {
        Game game = new Game();

        assertNotNull(game.getGrid());
        assertNotNull(game.getPlayerBlack());
        assertNotNull(game.getPlayerWhite());
    }
}

