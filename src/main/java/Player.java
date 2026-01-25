public enum Player {
    BLACK, WHITE;

    public Grid.CrossState toCross() {
        return switch (this) {
            case BLACK -> Grid.CrossState.BLACK;
            case WHITE -> Grid.CrossState.WHITE;
        };
    }

    //penso sia una funzione da eliminare perchè non utile
    public static Player fromCross(Grid.CrossState cross) {
        return switch (cross) {
            case BLACK -> BLACK;
            case WHITE -> WHITE;
            case EMPTY -> throw new IllegalArgumentException("EMPTY is not a player");
        };
    }

    public Player other() {
        return (this == BLACK) ? WHITE : BLACK;
    }
}
