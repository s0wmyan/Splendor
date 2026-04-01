package game;

// import engine.ActionValidator;
// import engine.EndGameManager;
import engine.TurnManager;
import engine.WinConditionChecker;

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
    // public void run() {
    //     // uses state and turnmanager

    //     while (!turnManager.hasWinner(state.getPlayers())) {
    //         turnManager.executeTurn(state);
    //     }

    //     System.out.println("Game has ended!");
    // }

    // main entry method
    public void run() {
        // WinConditionChecker checker = new WinConditionChecker(); // old method, doesn't load from config
        WinConditionChecker checker = turnManager.getWinConditionChecker();

        int playerCount = turnManager.getPlayerCount();

        boolean finalRound = false;
        int remainingTurnsInFinalRound = 0;

        while (true) {
            model.Player playerWhoStartsTurn = state.getCurrentPlayer();

            // executes the whole turn and then advances to next player
            turnManager.executeTurn(state);

            // If the game hasn't entered the final round yet,
            // check whether the player who just played reached checkpoint+.
            if (!finalRound) {
                if (playerWhoStartsTurn.getPrestigePoints() >= checker.getWinningPrestigeRequirement()) {
                    finalRound = true;
                    remainingTurnsInFinalRound = playerCount - 1;

                    if (remainingTurnsInFinalRound <= 0) {
                        break;
                    }
                }
            } else {
                // in the final round, consume exactly one remaining turn per loop.
                remainingTurnsInFinalRound--;
                if (remainingTurnsInFinalRound <= 0) {
                    break;
                }
            }
        }
        model.Player winner = checker.getWinner(state.getPlayers());
        if (winner != null) {
            System.out.println("Game has ended! Winner: " + winner.getName());
        } else {
            System.out.println("Game has ended! No winner determined.");
        }
    }
}

