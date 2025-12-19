public record Pawn(Color PLAYERCOLOR) {

    public enum Color {
        WHITE,
        BLACK
    }

    public Color getColor() {
        return PLAYERCOLOR;
    }

}
