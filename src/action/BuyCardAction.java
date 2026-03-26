package action;

import model.*;
import game.*;
import java.util.*;


public class BuyCardAction implements Action {
    private DevelopmentCard card;
    private boolean isReserved;

    public BuyCardAction(DevelopmentCard card, boolean isReserved) {
        this.card = card;
        this.isReserved = isReserved;
    }

    public boolean validate(GameState state) {
        if (card.equals(null)) {
            return false
        }
        
        Board board = state.getBoard();
        Player player = state.getCurrentPlayer(); // not getPlayer()

        if (!card.isValid()) return false;

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

        return canAfford(player);
    }

    public void apply(GameState state) {
        Board board = state.getBoard();
        Player player = state.getCurrentPlayer();

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

        player.getPurchasedCards().add(card);
        if (isReserved) {
            player.getReservedCards().remove(card);
        } else {
            int level = card.getLevel();
            if (board.hasCardsInDeck(level)) {
                board.refillLevel(level);
            }
        }

    }

    // no static 
    private boolean canAfford(Player player) {
        
        int goldNeeded = 0;
        Map<GemType, Integer> cardCost = card.getCost();

        for (Map.Entry<GemType, Integer> entry : cardCost.entrySet()) {
            GemType requiredToken = entry.getKey();
            int requiredAmount = entry.getValue();

            int bonus = player.getBonus(requiredToken); 
            int remainingCost = requiredAmount - bonus;
            if (remainingCost <= 0) {
                continue; 
            }

            int tokensOwned = player.getTokens().get(requiredToken);
            if (tokensOwned < remainingCost) {
                goldNeeded += (remainingCost - tokensOwned);
            }
        }

        return player.getTokens().get(GemType.GOLD) >= goldNeeded;
    }

    // ? 

    // /**
    //  * Attempts to execute the "Buy 1 Card" action.
    //  * Returns true if successful, false if illegal.
    //  */
    // private boolean attemptBuyCard(Player player, DevelopmentCard targetCard, boolean isReserved) {
    //     // 0. The Location Gatekeeper: Does this card actually exist where they claim it does?
    //     if (isReserved) {
    //         if (!player.getReservedCards().contains(targetCard)) {
    //             System.out.println("Security Error: This card is not in your reserved hand.");
    //             return false;
    //         }
    //     } else {
    //         int level = targetCard.getLevel();
    //         // there is no this.board. 
    //         if (!this.board.getFaceUpCards().get(level).contains(targetCard)) {
    //             System.out.println("Security Error: This card is not currently face-up on the board.");
    //             return false;
    //         }
    //     }
        
    //     // 1. The Gatekeeper: Can they actually afford it?
    //     if (!player.canAfford(targetCard.getCost())) {
    //         System.out.println("Invalid move: You do not have enough tokens/bonuses to afford this card.");
    //         return false;
    //     }

    //     // 2. The Payment Algorithm (Deducting from Player, Returning to Bank)
    //     for (Map.Entry<GemType, Integer> entry : targetCard.getCost().entrySet()) {
    //         GemType requiredToken = entry.getKey();
    //         int requiredAmount = entry.getValue();

    //         // Apply the discount!
    //         int bonus = player.getBonus(requiredToken);
    //         int remainingCost = requiredAmount - bonus;

    //         if (remainingCost > 0) {
    //             int tokensOwned = player.getTokens().get(requiredToken);
                
    //             // Prioritize standard gems
    //             int tokensToSpend = Math.min(remainingCost, tokensOwned);
    //             player.getTokens().put(requiredToken, tokensOwned - tokensToSpend);
                
    //             // Return spent gems to the Board's bank
    //             int bankGems = this.board.getBank().get(requiredToken);
    //             this.board.getBank().put(requiredToken, bankGems + tokensToSpend);
                
    //             // Handle the Gold shortfall
    //             int goldNeeded = remainingCost - tokensToSpend;
    //             if (goldNeeded > 0) {
    //                 int goldOwned = player.getTokens().get(GemType.GOLD);
    //                 player.getTokens().put(GemType.GOLD, goldOwned - goldNeeded);
                    
    //                 // Return spent Gold to the Board's bank
    //                 int bankGold = this.board.getBank().get(GemType.GOLD);
    //                 this.board.getBank().put(GemType.GOLD, bankGold + goldNeeded);
    //             }
    //         }
    //     }

    //     // 3. Transfer the card to the player's purchased list
    //     player.getPurchasedCards().add(targetCard);

    //     // 4. Remove the card from its original location
    //     if (isReserved) {
    //         player.getReservedCards().remove(targetCard);
    //     } else {
    //         int level = targetCard.getLevel();
    //         this.board.getFaceUpCards().get(level).remove(targetCard);
            
    //         // Refill the empty spot on the board if deck has cards left
    //         List<DevelopmentCard> deck = this.board.getDecks().get(level);
    //         if (!deck.isEmpty()) {
    //             this.board.getFaceUpCards().get(level).add(deck.remove(0));
    //         }
    //     }

    //     System.out.println("Card purchased successfully!");
    //     return true;
    // }
}