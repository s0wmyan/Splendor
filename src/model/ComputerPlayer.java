package model;

import action.*;
import game.*;
import java.util.*;

public class ComputerPlayer extends Player {

    private Random random;

    public ComputerPlayer(String name) {
        super(name, new Birthday(1, 1, 2000)); // bday unimpt, will not be used
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

        // Priority 3: Try to take 3 different gems
        List<GemType> availableStandardGems = new ArrayList<>(GemType.getStandardGems());
        Collections.shuffle(availableStandardGems, random); // shuffle it

        // First, check how many unique colors the bakn has available
        int availableColours = 0;

        for (GemType gem : availableStandardGems) {
            if (board.getBank().getTokenCount(gem) > 0) {
                availableColours++;
            }
        }

        if (availableColours >= 3) {
            Map<GemType, Integer> gemsToTake = new HashMap<>();
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
            return new TakeGemsAction(gemsToTake);
        }

        // Priority 4: Try to take 2 of the SAME gems
        for (GemType gem : availableStandardGems) {
            if (board.getBank().getTokenCount(gem) >= 4) {
                Map<GemType, Integer> gemsToTake = new HashMap<>();
                gemsToTake.put(gem, 2);
                return new TakeGemsAction(gemsToTake);
            }
        }

        // Priority 5: if cant take gems just reserve (to avoid crash)
        List<DevelopmentCard> faceUpCards = board.allFaceUpCards();

        if (!faceUpCards.isEmpty()) {
            DevelopmentCard randomCard = faceUpCards.get(random.nextInt(faceUpCards.size()));
            return new ReserveCardAction(randomCard);
        }

        return null; // worst case scenario, should never happen
    }
}
