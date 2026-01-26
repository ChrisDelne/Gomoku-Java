public enum MoveResult {
    VALID_MOVE(true, "Fair move"),
    OUT_OF_BOUNDS(false, "Position is out of bounds"),
    POSITION_OCCUPIED(false, "Position is already occupied");

    private final boolean valid;
    private final String reasonWhy;

    MoveResult(boolean valid, String reasonWhy) {
        this.valid = valid;
        this.reasonWhy = reasonWhy;
    }

    public boolean isValid() {
        return valid;
    }

    public String getReason() {
        return reasonWhy;
    }

}
