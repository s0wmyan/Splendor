package game.action;

public interface Action {
    public boolean validate(GameState state, Board board);

    public void apply(GameState state, Board board);
}