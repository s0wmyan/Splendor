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

    public boolean validate(GameState state, Board board) {
        Player player = state.getCurrentPlayer();
        
        if (player.getReservedCards().size() >= 3) {
            return false;
        }

        if (!fromDeck) {
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
        } else {
            return board.hasCardsInDeck(cardLevel);
        }

        return true;
    }


    public void apply(GameState state, Board board) {
        Player player = state.getCurrentPlayer();

        if (fromDeck) {
            DevelopmentCard cardTaken = board.drawFromDeck(cardLevel);
            player.getReservedCards().add(cardTaken);
        } else {
            List<DevelopmentCard> faceUpCards = null;

            switch (cardLevel) {
                case 1:
                    faceUpCards = board.getLevel1FaceUp();
                    break;

                case 2:
                    faceUpCards = board.getLevel2FaceUp();
                    break;

                default:
                    faceUpCards = board.getLevel3FaceUp();
                    break;
            }

            int index = faceUpCards.indexOf(card);
            DevelopmentCard cardTaken = board.takeFaceUpCard(cardLevel, index);
            player.getReservedCards().add(cardTaken);

            if (board.hasCardsInDeck(cardLevel)) {
                board.refillLevel(cardLevel);
            }

            if (state.getBank().getTokenCount(GemType.GOLD) > 0) {
                state.getBank().removeTokenCount(GemType.GOLD, 1); // ? 
                int yellowCount = player.getTokens().get(GemType.GOLD);
                player.getTokens().put(GemType.GOLD, yellowCount + 1);
            }
        }

    }
}
