package action;

import model.*;
import game.*;
import java.util.*;


public class ReserveCardAction implements Action {
    private DevelopmentCard card;
    private int cardLevel;
    private boolean luckyReserve;

    public ReserveCardAction(DevelopmentCard card) {
        this.card = card;
        this.cardLevel = card.getLevel();
        this.luckyReserve = false; // rule where player randomly reserves a card from facedowncards
    }

    public ReserveCardAction(int cardLevel) {
        this.cardLevel = cardLevel;
        this.luckyReserve = true;
    }

    public String validate(GameState state) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();
        
        // player can only reserve max 3 cards)
        if (player.getReservedCards().size() >= 3) {
            return "You can only have a maximum of 3 reserved cards";
        }

        // check if board has selected card 

        if (luckyReserve) {
            if (!board.hasCardsInDeck(cardLevel)) {
                return "No more cards left in deck to perform a lucky reserve!";
            }
        } else {
            if (!board.containsFaceUpCard(card)) {
                return "Card does not exist in the faceup cards";
            }
        }
        
        return null; // no errors 
    }


    // command query separation: method should only modify state or return data, not both at the same time. 
    // separate apply and getSuccessMessage()
    public void apply(GameState state) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();


        if (luckyReserve) { // reserve from deck
            DevelopmentCard cardTaken = board.drawFromDeck(cardLevel);
            player.getReservedCards().add(cardTaken);
        } else { // reserve from board
            board.removeFaceUpCard(cardLevel, card);
            player.getReservedCards().add(card);

            // refill board if deck still has cards
            if (board.hasCardsInDeck(cardLevel)) {
                board.refillLevel(cardLevel);
            }

        }

        // give player a gold token if bank still has it
        if (state.getBank().getTokenCount(GemType.GOLD) > 0) {
            state.getBank().removeTokenCount(GemType.GOLD, 1); 
            int goldCount = player.getTokens().get(GemType.GOLD);
            player.getTokens().put(GemType.GOLD, goldCount + 1);
        }

    }


    public String getSuccessMessage(GameState state) {
        Player player = state.getCurrentPlayer();
        return "Player " + player.getName() + " has reserved the card " + card;
    }
}
