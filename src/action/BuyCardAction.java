package action;

import game.*;
import java.util.*;
import model.*;
import util.CardUtil;


public class BuyCardAction implements Action {
    private DevelopmentCard card;
    private boolean isReserved;

    public BuyCardAction(DevelopmentCard card, boolean isReserved) {
        this.card = card;
        this.isReserved = isReserved;
    }

    // Edited this part
    public String validate(GameState state) {
        // check if card is null
        if (card == null) {
            return "There is no available card to purchase!"; // duplicate message
        }
        
        Board board = state.getBoard();
        Player player = state.getCurrentPlayer();

        // check whether the card is present 
        // Changed the logic so that you can buy the cards you reserve
        if (isReserved) {
            if (!player.getReservedCards().contains(card)) {
                return "You did not reserve this card before!";
            }
        } else {
            if (!board.containsFaceUpCard(card)) {
                return "Card does not exist in the faceup cards!"; // which shouldn't happen because there is a check already happening 
            }
        }

        // check if player can afford the card
        if (!player.canAfford(card.getCost())) {
            return "You cannot afford this card!";
        }

        return null;
    }

    // Edited this part
    public void apply(GameState state) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();

        // pay the cost
        for (Map.Entry<GemType, Integer> entry : card.getCost().entrySet()) {
            GemType requiredToken = entry.getKey();
            int requiredAmount = entry.getValue();
            int bonus = player.getBonus(requiredToken);
            int remainingCost = requiredAmount - bonus;

            if (remainingCost <= 0) continue;

            int tokensOwned = player.getTokens().get(requiredToken);
            int tokensToSpend = Math.min(remainingCost, tokensOwned);

            player.getTokens().put(requiredToken, tokensOwned - tokensToSpend);
            board.getBank().addTokenCount(requiredToken, tokensToSpend);

            int goldNeeded = remainingCost - tokensToSpend;
            if (goldNeeded > 0) {
                int goldOwned = player.getTokens().get(GemType.GOLD);
                player.getTokens().put(GemType.GOLD, goldOwned - goldNeeded);
                
                board.getBank().addTokenCount(GemType.GOLD, goldNeeded);
            }
            
        }


        // transfer card from reserve/board to player's purchased cards
        if (isReserved) {
            player.getReservedCards().remove(card); // then what?
        } else {
            int level = card.getLevel();
            board.removeFaceUpCard(level, card);
        }
        player.addPurchasedCard(card);

    }

    public String getSuccessMessage(GameState state) {
        Player player = state.getCurrentPlayer();
        return "Player " + player.getName() + " has bought the card: " + CardUtil.formatCardDetails(card);
    }
}