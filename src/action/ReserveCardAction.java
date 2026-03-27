package action;

import model.*;
import game.*;
import java.util.*;


public class ReserveCardAction implements Action {
    private DevelopmentCard card;
    private int cardLevel;
    private boolean fromDeck;

    public ReserveCardAction(DevelopmentCard card) {
        this.card = card;
        this.cardLevel = card.getLevel();
        this.fromDeck = false;
    }

    public ReserveCardAction(int cardLevel) {
        this.cardLevel = cardLevel;
        this.fromDeck = true;
    }

    public boolean validate(GameState state) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();
        
        // plaayer can only reserve max 3 cards)
        if (player.getReservedCards().size() >= 3) {
            return false;
        }

        
        if (!fromDeck) { // chack if board has selected card
            List<DevelopmentCard> faceUpCards = null;

            switch (cardLevel) {
                case 1:
                    faceUpCards = board.getLevel1FaceUp();
                    break;

                case 2:
                    faceUpCards = board.getLevel2FaceUp();
                    break;

                case 3:
                    faceUpCards = board.getLevel3FaceUp();
                    break;

                default:
                    return false;

            }

            if (!faceUpCards.contains(card)) {
                    return false;
            }
        } else { // check if deck has cards to blindly reserve from
            return board.hasCardsInDeck(cardLevel);
        }

        return true;
    }


    public void apply(GameState state) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();


        if (fromDeck) { // reserve from deck
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
}
