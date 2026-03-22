package model;

// noble should not be in deck -> it is fixed
import java.util.*;
import model.DevelopmentCard;

public class Deck {
    private int level; // tier
    private List<DevelopmentCard> cards;

    public Deck(int level, List<DevelopmentCard> cards) {
        this.level = level;
        this.cards = cards;
    }

    public int getLevel() {
        return level;
    }

    public List<DevelopmentCard> getCards() {
        return cards;
    }

    public int getSize() {
        return cards.size(); // size of the deck 
    }

    public boolean isEmpty() {
        return cards.size() == 0;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public DevelopmentCard drawCard() {
        // draws from the deck 
        return cards.remove(cards.size() - 1); // remove from the end
    }

}
