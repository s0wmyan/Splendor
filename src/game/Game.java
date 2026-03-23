package game;

// import engine.ActionValidator;
// import engine.EndGameManager;
import engine.*;
import java.util.List;

public class Game {

    private GameState state;
    private TurnManager turnManager;
    // private ActionValidator validator;
    // private ActionExecutor executor;
    // private EndGameManager endGameManager;

    public Game(GameState state, TurnManager turnManager) {
        this.state = state;
        this.turnManager = turnManager;
        // this.turnManager = new TurnManager(); => turn manager now under game state
        // game instantiates turn manager? 
        
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
        // uses state and turnmanager

        while (!turnManager.hasWinner(state.getPlayers())) {
            turnManager.executeTurn(state);
        }

        System.out.println("Game has ended!");
    }
}

