package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Board {

    public static final int FACE_UP_PER_LEVEL = 4;

    public Bank bank = new Bank();
    private final Deck level1Deck;
    private final Deck level2Deck;
    private final Deck level3Deck;

    private final List<DevelopmentCard> level1FaceUp = new ArrayList<>();
    private final List<DevelopmentCard> level2FaceUp = new ArrayList<>();
    private final List<DevelopmentCard> level3FaceUp = new ArrayList<>();

    private final List<Noble> nobles = new ArrayList<>();

    public Board(Deck level1Deck, Deck level2Deck, Deck level3Deck, List<Noble> nobles) {
        this.level1Deck = level1Deck;
        this.level2Deck = level2Deck;
        this.level3Deck = level3Deck;
        this.nobles.addAll(nobles);
    }

    public Bank getBank() {
        return bank;
    }

    public List<DevelopmentCard> getLevel1FaceUp() {
        return Collections.unmodifiableList(level1FaceUp);
    }

    public List<DevelopmentCard> getLevel2FaceUp() {
        return Collections.unmodifiableList(level2FaceUp);
    }

    public List<DevelopmentCard> getLevel3FaceUp() {
        return Collections.unmodifiableList(level3FaceUp);
    }

    public List<Noble> getNobles() {
        return Collections.unmodifiableList(nobles);
    }

    public void removeNoble(Noble noble) {
        nobles.remove(noble);
    }

    public void refillAll() {
        refillLevel(level1Deck, level1FaceUp);
        refillLevel(level2Deck, level2FaceUp);
        refillLevel(level3Deck, level3FaceUp);
    }

    public void refillLevel(int level) {
        switch (level) {
            case 1 -> refillLevel(level1Deck, level1FaceUp);
            case 2 -> refillLevel(level2Deck, level2FaceUp);
            case 3 -> refillLevel(level3Deck, level3FaceUp);
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    private void refillLevel(Deck deck, List<DevelopmentCard> faceUp) {
        while (faceUp.size() < FACE_UP_PER_LEVEL && !deck.isEmpty()) {
            DevelopmentCard drawn = deck.drawCard();
            if (drawn != null) {
                faceUp.add(drawn);
            }
        }
    }

    public DevelopmentCard takeFaceUpCard(int level, int index) {
        List<DevelopmentCard> list = switch (level) {
            case 1 -> level1FaceUp;
            case 2 -> level2FaceUp;
            case 3 -> level3FaceUp;
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        };
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException("No card at index " + index + " for level " + level);
        }
        return list.remove(index);
    }

    public DevelopmentCard drawFromDeck(int level) {
        return switch (level) {
            case 1 -> level1Deck.isEmpty() ? null : level1Deck.drawCard();
            case 2 -> level2Deck.isEmpty() ? null : level2Deck.drawCard();
            case 3 -> level3Deck.isEmpty() ? null : level3Deck.drawCard();
            default -> throw new IllegalArgumentException("Invalid level: " + level);
        };
    }

    public boolean hasCardsInDeck(int level) {
        return switch (level) {
            case 1 -> !level1Deck.isEmpty();
            case 2 -> !level2Deck.isEmpty();
            case 3 -> !level3Deck.isEmpty();
            default -> false;
        };
    }

    public List<DevelopmentCard> allFaceUpCards() {

        List<DevelopmentCard> tier1FaceUpCards = getLevel1FaceUp();
        List<DevelopmentCard> tier2FaceUpCards = getLevel2FaceUp();
        List<DevelopmentCard> tier3FaceUpCards = getLevel3FaceUp();

        List<DevelopmentCard> allFaceUpCards = new ArrayList<>();

        allFaceUpCards.addAll(tier1FaceUpCards);
        allFaceUpCards.addAll(tier2FaceUpCards);
        allFaceUpCards.addAll(tier3FaceUpCards);

        return allFaceUpCards;
    }

    public Deck getDeck(int deckNo) {
        return switch (deckNo) {
            case 1 -> level1Deck;
            case 2 -> level2Deck;
            case 3 -> level3Deck;
            default -> throw new IllegalArgumentException("Invalid deck: " + deckNo);
        };
    }

    public int getTokenCount(GemType type){
        return bank.getTokenCount(type);
    }

    public Map<GemType, Integer> getTokens(){
        return bank.getTokens();
    }
}

