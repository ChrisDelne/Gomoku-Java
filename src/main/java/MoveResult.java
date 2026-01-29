public enum MoveResult {
    VALID_MOVE(true, "Mossa valida"),
    OUT_OF_BOUNDS(false, "Posizione fuori griglia"),
    POSITION_OCCUPIED(false, "Posizione già occupata");

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
