public enum Player {
    BLACK, WHITE;

    public CrossState toCross() {
        return switch (this) {
            case BLACK -> CrossState.BLACK;
            case WHITE -> CrossState.WHITE;
        };
    }

    public Player other() {
        return (this == BLACK) ? WHITE : BLACK;
    }
}
