public class MoveService {

    private final Grid grid;

    public MoveService(Grid grid) {
        this.grid = grid;
    }

    public MoveResult makeMove(Player player, Grid.Position position) {
        if (!grid.contains(position))
            return MoveResult.OUT_OF_BOUNDS;
        if (!grid.isEmpty(position))
            return MoveResult.POSITION_OCCUPIED;
        if (player == Player.BLACK)
            grid.occupyCrossWithBlack(position);
        else
            grid.occupyCrossWithWhite(position);
        return MoveResult.VALID_MOVE;
    }

}
