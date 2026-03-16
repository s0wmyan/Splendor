package model;

import game.action.*;
import game.game.*;
import game.ui.*;
import java.util.*;

// Uses a basic rules-based algorithm to choose its actions.
public class ComputerPlayer extends Player {

    private Random random;

    // Constructor
    public ComputerPlayer(String name, Birthday birthday) {
        super(name, birthday); 
        this.random = new Random();
    }

    // --- Overridden Methods ---
    
    /**
     1. The computer looks at the current game state and decides what to do
     2. Priority 1: Buy a card if it can afford one.
     3. Priority 2: Take 3 different gems to save up.
     */
    @Override
    public Action takeTurn(GameState gameState) {
        Board board = gameState.getBoard();

        // 1. Try to buy a card
        // Loop through all visible cards on the board (Levels 1, 2, and 3)
        List<DevelopmentCard> allVisibleCards = new ArrayList<>();
        allVisibleCards.addAll(board.getLevel1FaceUp());
        allVisibleCards.addAll(board.getLevel2FaceUp());
        allVisibleCards.addAll(board.getLevel3FaceUp());

        for (DevelopmentCard card : allVisibleCards) {
            // Reusing the canAfford logic from the parent Player class!
            if (this.canAfford(card.getCost())) {
                // Return an action object representing the decision to buy
                return new BuyCardAction(card);
            }
        }

        // 2. If it can't afford any cards, take gems.
        // For simplicity, we'll try to grab up to 3 different standard gems from the bank.
        List<GemType> gemsToTake = new ArrayList<>();
        List<GemType> availableStandardGems = GemType.getStandardGems();
        
        // Shuffle the list to randomize which colors the AI picks
        java.util.Collections.shuffle(availableStandardGems, random);

        for (GemType gem : availableStandardGems) {
            // Check if the bank actually has this gem in stock
            if (board.getSupplyTokens().get(gem) > 0) {
                gemsToTake.add(gem);
            }
            // Stop once we have chosen 3 different gems
            if (gemsToTake.size() == 3) {
                break;
            }
        }

        // Return the action representing the decision to take gems
        return new TakeGemAction(gemsToTake);
    }
}
