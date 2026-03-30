package action;

import game.*;
import model.*;

public interface Action {
    public String validate(GameState state);

    public void apply(GameState state);
}