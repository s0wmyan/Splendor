package splendor.model;

import splendor.action.*;
import splendor.game.*;
import splendor.ui.*;

public class HumanPlayer extends Player {

    // The UI manager passed in so the player can communicate with the console
    private ConsoleManager consoleManager;

    // Constructor
    public HumanPlayer(String name, Birthday birthday, ConsoleManager consoleManager) {
        super(name, birthday); 
        this.consoleManager = consoleManager;
    }
    
    @Override
    public Action takeTurn(GameState gameState) {
        
        // Player's turn. Board state provided.
        // Ask the human sitting at the keyboard what they want to do, 
        // and give me back an Action object representing their choice
        
        return consoleManager.promptForAction(this, gameState);
    }
}