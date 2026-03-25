package action;

import game.*;
import model.*;

public interface Action {
    public boolean validate(GameState state);

    public void apply(GameState state);
}