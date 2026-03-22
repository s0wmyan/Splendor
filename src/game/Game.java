package game;

import engine.TurnManager;
// import engine.ActionValidator;
// import engine.EndGameManager;

import action.*;

import model.Noble;
import model.Player;

import java.util.List;

public class Game {

    private GameState state;
    private TurnManager turnManager;
    // private ActionValidator validator;
    // private ActionExecutor executor;
    // private EndGameManager endGameManager;

    public Game(GameState state) {
        this.state = state;
        this.turnManager = new TurnManager();
        // this.validator = new ActionValidator();
        // this.executor = new ActionExecutor();
        // this.endGameManager = new EndGameManager();
    }

    public GameState getState() {
        return state;
    }

    // public boolean isGameOver() {
    //     return endGameManager.isGameOver();
    // }

    // public ValidationResult validateAction(GameAction action) {
    //     return validator.validate(state, getCurrentPlayerIndex(), action);
    // }

    // public void applyAction(GameAction action) {
    //     executor.execute(state, getCurrentPlayerIndex(), action);
    // }

    // public Player determineWinner() {
    //     return endGameManager.determineWinner(state.getPlayers());
    // }

    // main entry method
    public void run() {
        
    }
}

