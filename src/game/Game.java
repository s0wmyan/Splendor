package game;

import engine.TurnManager;
import engine.TurnManager;
import engine.ActionValidator;
import engine.ActionExecutor;
import engine.EndGameManager;

import action.GameAction;

import model.Noble;
import model.Player;

import java.util.List;

public class Game {

    private GameState state;
    private TurnManager turnManager;
    private ActionValidator validator;
    private ActionExecutor executor;
    private EndGameManager endGameManager;

    public Game(GameState state) {
        this.state = state;
        this.turnManager = new TurnManager();
        this.validator = new ActionValidator();
        this.executor = new ActionExecutor();
        // this.endGameManager = new EndGameManager();
    }

    public GameState getState() {
        return state;
    }

    public Player getCurrentPlayer() {
        return state.getPlayers().get(turnManager.getCurrentPlayerIndex());
    }

    public int getCurrentPlayerIndex() {
        return turnManager.getCurrentPlayerIndex();
    }

    public boolean isGameOver() {
        return endGameManager.isGameOver();
    }

    public ValidationResult validateAction(GameAction action) {
        return validator.validate(state, getCurrentPlayerIndex(), action);
    }

    public void applyAction(GameAction action) {
        executor.execute(state, getCurrentPlayerIndex(), action);
    }

    public Player determineWinner() {
        return endGameManager.determineWinner(state.getPlayers());
    }
}

