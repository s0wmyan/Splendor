package model;

import action.*;
import game.*;
import java.util.*;

public class ComputerPlayer extends Player {

    private Random random;

    public ComputerPlayer(String name, Birthday birthday) {
        super(name, birthday); 
        this.random = new Random();
    }
    
    @Override
    public Action takeTurn(GameState gameState) {
        Board board = gameState.getBoard();

        // Priority 1: Try to buy a reserved card from its own hand
        for (DevelopmentCard card : this.getReservedCards()) {
            if (this.canAfford(card.getCost())) {
                // buy card
                return new BuyCardAction(card, true); 
            }
        }

        // Priority 2: Try to buy a face-up card from the board
        List<DevelopmentCard> allVisibleCards = board.allFaceUpCards();
        for (DevelopmentCard card : allVisibleCards) {
            if (this.canAfford(card.getCost())) {
                // isReserved is false since it's from the board
                return new BuyCardAction(card, false); 
            }
        }

        // Priority 3: If it can't afford any cards, take up to 3 different gems
        Map<GemType, Integer> gemsToTake = new HashMap<>();
        for (GemType type : GemType.values()) {
            gemsToTake.put(type, 0); // Initialize map with 0s to prevent NullPointerException
        }

        // Get a list of just the standard gems so we can safely shuffle without breaking the original enum
        List<GemType> availableStandardGems = new ArrayList<>(GemType.getStandardGems());

        // Shuffle so the computer doesn't just pick Red, Blue, White every single time
        Collections.shuffle(availableStandardGems, random);

        int gemsTaken = 0; // tracker
        for (GemType gem : availableStandardGems) { // loop through newly shuffled list
            // if bank has more than 0, computer takes one
            if (board.getBank().getTokenCount(gem) > 0) { 
                gemsToTake.put(gem, 1); // record that we took one
                gemsTaken++; // increase counter
            }
            if (gemsTaken == 3) { // once we hit 3, stop
                break;
            }
        }

        // take gems chosen
        return new TakeGemsAction(gemsToTake);
    }
}
