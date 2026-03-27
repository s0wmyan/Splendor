package action;

import game.*;
import java.util.*;
import model.*;


public class BuyCardAction implements Action {
    private DevelopmentCard card;
    private boolean isReserved;

    public BuyCardAction(DevelopmentCard card, boolean isReserved) {
        this.card = card;
        this.isReserved = isReserved;
    }

    public boolean validate(GameState state) {
        // check if card is null
        if (card == null) {
            return false;
        }
        
        Board board = state.getBoard();
        Player player = state.getCurrentPlayer();

        // check if card is where the player intends to buy from
        if (isReserved) {
            if (!player.getReservedCards().contains(card)) {
                return false;
            }
        } else {
            int level = card.getLevel();
            switch (level) {
                case 1:
                    if (!board.getLevel1FaceUp().contains(card)) {
                        return false;
                    }
                    break;

                case 2:
                    if (!board.getLevel2FaceUp().contains(card)) {
                        return false;
                    }
                    break;

                case 3:
                    if (!board.getLevel3FaceUp().contains(card)) {
                        return false;
                    }
                    break;
                
                default:
                    return false;
            }
        }

        // check if player can afford the card
        return player.canAfford(card.getCost());
    }

    public void apply(GameState state) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();

        // pay the cost
        for (Map.Entry<GemType, Integer> entry : card.getCost().entrySet()) {
            GemType requiredToken = entry.getKey();
            int requiredAmount = entry.getValue();

            int bonus = player.getBonus(requiredToken);
            int remainingCost = requiredAmount - bonus;

            if (remainingCost > 0) {
                int tokensOwned = player.getTokens().get(requiredToken);
                
                int tokensToSpend = (remainingCost > tokensOwned) ? tokensOwned : remainingCost;
                player.getTokens().put(requiredToken, tokensOwned - tokensToSpend);
                
                board.getBank().addTokenCount(requiredToken, tokensToSpend);
                
                int goldNeeded = remainingCost - tokensToSpend;
                if (goldNeeded > 0) {
                    int goldOwned = player.getTokens().get(GemType.GOLD);
                    player.getTokens().put(GemType.GOLD, goldOwned - goldNeeded);
                    
                    board.getBank().addTokenCount(GemType.GOLD, goldNeeded);
                }
            }
            
        }


        // transfer card from reserve/board to player's purchased cards
        if (isReserved) {
            player.getReservedCards().remove(card); // then what?
        } else {
            int level = card.getLevel();
            board.removeFaceUpCard(level, card);

            // refill board if deck still has cards
            if (board.hasCardsInDeck(level)) {
                board.refillLevel(level);

            }
        }
        player.addPurchasedCard(card);

    }

    // supposed canAfford method (transfered to Player class)
    // private boolean canAfford(Player player) {
        
    //     int goldNeeded = 0;
    //     Map<GemType, Integer> cardCost = card.getCost();

    //     for (Map.Entry<GemType, Integer> entry : cardCost.entrySet()) {
    //         GemType requiredToken = entry.getKey();
    //         int requiredAmount = entry.getValue();

    //         int bonus = player.getBonus(requiredToken); 
    //         int remainingCost = requiredAmount - bonus;
    //         if (remainingCost <= 0) {
    //             continue; 
    //         }

    //         int tokensOwned = player.getTokens().get(requiredToken);
    //         if (tokensOwned < remainingCost) {
    //             goldNeeded += (remainingCost - tokensOwned);
    //         }
    //     }

    //     return player.getTokens().get(GemType.GOLD) >= goldNeeded;
    // }
}