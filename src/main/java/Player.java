public enum Player {
    BLACK, WHITE;

    public Grid.Cross toCross() {
        return switch (this) {
            case BLACK -> Grid.Cross.BLACK;
            case WHITE -> Grid.Cross.WHITE;
        };
    }

    public static Player fromCross(Grid.Cross cross) {
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
