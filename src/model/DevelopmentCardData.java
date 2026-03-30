package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DevelopmentCardData {

    public static List<DevelopmentCard> getCardsByLevel(int level) {
        if (level == 1) return getLevel1Cards();
        if (level == 2) return getLevel2Cards();
        if (level == 3) return getLevel3Cards();
        throw new IllegalArgumentException("Invalid deck level: " + level);
    }

    public static List<DevelopmentCard> getLevel1Cards() {
        List<DevelopmentCard> cards = new ArrayList<>();

        // WHITE bonus
        cards.add(new DevelopmentCard(1, GemType.WHITE, Map.of(GemType.BLUE, 1, GemType.BLACK, 2), 0));
        cards.add(new DevelopmentCard(1, GemType.WHITE, Map.of(GemType.GREEN, 1, GemType.RED, 1, GemType.BLACK, 1, GemType.BLUE, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.WHITE, Map.of(GemType.GREEN, 2, GemType.RED, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.WHITE, Map.of(GemType.RED, 2, GemType.BLACK, 2), 0));

        // BLUE bonus
        cards.add(new DevelopmentCard(1, GemType.BLUE, Map.of(GemType.WHITE, 1, GemType.RED, 1, GemType.BLACK, 1, GemType.GREEN, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.BLUE, Map.of(GemType.WHITE, 2, GemType.RED, 2), 0));
        cards.add(new DevelopmentCard(1, GemType.BLUE, Map.of(GemType.BLACK, 3), 0));
        cards.add(new DevelopmentCard(1, GemType.BLUE, Map.of(GemType.WHITE, 1, GemType.BLACK, 2), 0));

        // GREEN bonus
        cards.add(new DevelopmentCard(1, GemType.GREEN, Map.of(GemType.BLUE, 1, GemType.WHITE, 1, GemType.RED, 1, GemType.BLACK, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.GREEN, Map.of(GemType.BLUE, 2, GemType.BLACK, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.GREEN, Map.of(GemType.BLUE, 1, GemType.RED, 3), 0));
        cards.add(new DevelopmentCard(1, GemType.GREEN, Map.of(GemType.WHITE, 2, GemType.BLUE, 1), 0));

        // RED bonus
        cards.add(new DevelopmentCard(1, GemType.RED, Map.of(GemType.WHITE, 1, GemType.BLUE, 1, GemType.GREEN, 1, GemType.BLACK, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.RED, Map.of(GemType.WHITE, 2, GemType.GREEN, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.RED, Map.of(GemType.GREEN, 2, GemType.BLACK, 2), 0));
        cards.add(new DevelopmentCard(1, GemType.RED, Map.of(GemType.BLUE, 2, GemType.GREEN, 1), 0));

        // BLACK bonus
        cards.add(new DevelopmentCard(1, GemType.BLACK, Map.of(GemType.WHITE, 1, GemType.BLUE, 1, GemType.GREEN, 1, GemType.RED, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.BLACK, Map.of(GemType.GREEN, 2, GemType.BLUE, 1), 0));
        cards.add(new DevelopmentCard(1, GemType.BLACK, Map.of(GemType.WHITE, 3), 0));
        cards.add(new DevelopmentCard(1, GemType.BLACK, Map.of(GemType.RED, 2, GemType.WHITE, 1), 0));

        return cards;
    }

    public static List<DevelopmentCard> getLevel2Cards() {
        List<DevelopmentCard> cards = new ArrayList<>();

        // WHITE bonus
        cards.add(new DevelopmentCard(2, GemType.WHITE, Map.of(GemType.BLACK, 5), 3));
        cards.add(new DevelopmentCard(2, GemType.WHITE, Map.of(GemType.BLUE, 2, GemType.GREEN, 3, GemType.BLACK, 2), 1));
        cards.add(new DevelopmentCard(2, GemType.WHITE, Map.of(GemType.GREEN, 1, GemType.RED, 4, GemType.BLACK, 2), 2));
        cards.add(new DevelopmentCard(2, GemType.WHITE, Map.of(GemType.RED, 2, GemType.GREEN, 1, GemType.BLUE, 1, GemType.BLACK, 1), 1));

        // BLUE bonus
        cards.add(new DevelopmentCard(2, GemType.BLUE, Map.of(GemType.WHITE, 5), 3));
        cards.add(new DevelopmentCard(2, GemType.BLUE, Map.of(GemType.WHITE, 2, GemType.RED, 3, GemType.BLACK, 2), 1));
        cards.add(new DevelopmentCard(2, GemType.BLUE, Map.of(GemType.WHITE, 2, GemType.GREEN, 2, GemType.BLACK, 3), 2));
        cards.add(new DevelopmentCard(2, GemType.BLUE, Map.of(GemType.WHITE, 1, GemType.GREEN, 1, GemType.RED, 1, GemType.BLACK, 2), 1));

        // GREEN bonus
        cards.add(new DevelopmentCard(2, GemType.GREEN, Map.of(GemType.RED, 5), 3));
        cards.add(new DevelopmentCard(2, GemType.GREEN, Map.of(GemType.BLUE, 2, GemType.WHITE, 3, GemType.RED, 2), 1));
        cards.add(new DevelopmentCard(2, GemType.GREEN, Map.of(GemType.BLUE, 3, GemType.WHITE, 1, GemType.BLACK, 2), 2));
        cards.add(new DevelopmentCard(2, GemType.GREEN, Map.of(GemType.BLUE, 2, GemType.WHITE, 1, GemType.BLACK, 1, GemType.RED, 1), 1));

        // RED bonus
        cards.add(new DevelopmentCard(2, GemType.RED, Map.of(GemType.GREEN, 5), 3));
        cards.add(new DevelopmentCard(2, GemType.RED, Map.of(GemType.WHITE, 1, GemType.BLUE, 3, GemType.GREEN, 2, GemType.BLACK, 1), 1));
        cards.add(new DevelopmentCard(2, GemType.RED, Map.of(GemType.WHITE, 3, GemType.BLUE, 2, GemType.GREEN, 1), 2));
        cards.add(new DevelopmentCard(2, GemType.RED, Map.of(GemType.BLUE, 1, GemType.GREEN, 2, GemType.BLACK, 2, GemType.WHITE, 1), 1));

        // BLACK bonus
        cards.add(new DevelopmentCard(2, GemType.BLACK, Map.of(GemType.BLUE, 5), 3));
        cards.add(new DevelopmentCard(2, GemType.BLACK, Map.of(GemType.WHITE, 2, GemType.BLUE, 1, GemType.GREEN, 2, GemType.RED, 3), 1));
        cards.add(new DevelopmentCard(2, GemType.BLACK, Map.of(GemType.WHITE, 2, GemType.RED, 1, GemType.GREEN, 4), 2));
        cards.add(new DevelopmentCard(2, GemType.BLACK, Map.of(GemType.WHITE, 1, GemType.BLUE, 1, GemType.GREEN, 1, GemType.RED, 2), 1));

        return cards;
    }

    public static List<DevelopmentCard> getLevel3Cards() {
        List<DevelopmentCard> cards = new ArrayList<>();

        // WHITE bonus
        cards.add(new DevelopmentCard(3, GemType.WHITE, Map.of(GemType.BLACK, 7), 4));
        cards.add(new DevelopmentCard(3, GemType.WHITE, Map.of(GemType.BLACK, 6, GemType.RED, 3), 5));
        cards.add(new DevelopmentCard(3, GemType.WHITE, Map.of(GemType.BLUE, 3, GemType.GREEN, 3, GemType.RED, 3, GemType.BLACK, 3), 3));

        // BLUE bonus
        cards.add(new DevelopmentCard(3, GemType.BLUE, Map.of(GemType.WHITE, 7), 4));
        cards.add(new DevelopmentCard(3, GemType.BLUE, Map.of(GemType.WHITE, 6, GemType.BLACK, 3), 5));
        cards.add(new DevelopmentCard(3, GemType.BLUE, Map.of(GemType.WHITE, 3, GemType.GREEN, 3, GemType.RED, 3, GemType.BLACK, 3), 3));

        // GREEN bonus
        cards.add(new DevelopmentCard(3, GemType.GREEN, Map.of(GemType.BLUE, 7), 4));
        cards.add(new DevelopmentCard(3, GemType.GREEN, Map.of(GemType.BLUE, 6, GemType.WHITE, 3), 5));
        cards.add(new DevelopmentCard(3, GemType.GREEN, Map.of(GemType.WHITE, 3, GemType.BLUE, 3, GemType.RED, 3, GemType.BLACK, 3), 3));

        // RED bonus
        cards.add(new DevelopmentCard(3, GemType.RED, Map.of(GemType.GREEN, 7), 4));
        cards.add(new DevelopmentCard(3, GemType.RED, Map.of(GemType.GREEN, 6, GemType.BLUE, 3), 5));
        cards.add(new DevelopmentCard(3, GemType.RED, Map.of(GemType.WHITE, 3, GemType.BLUE, 3, GemType.GREEN, 3, GemType.BLACK, 3), 3));

        // BLACK bonus
        cards.add(new DevelopmentCard(3, GemType.BLACK, Map.of(GemType.RED, 7), 4));
        cards.add(new DevelopmentCard(3, GemType.BLACK, Map.of(GemType.RED, 6, GemType.GREEN, 3), 5));
        cards.add(new DevelopmentCard(3, GemType.BLACK, Map.of(GemType.WHITE, 3, GemType.BLUE, 3, GemType.GREEN, 3, GemType.RED, 3), 3));

        return cards;
    }
}

// put this method under deck (should be discussed)
// public Deck(int level) {
//         this.level = level;
//         this.cards = new ArrayList<>(DevelopmentCardData.getCardsByLevel(level));
//         shuffle();
//     }