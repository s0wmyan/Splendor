package action;

import game.*;
import model.*;

public interface Action {
    public boolean validate(GameState state, Board board);

    public void apply(GameState state, Board board);
}