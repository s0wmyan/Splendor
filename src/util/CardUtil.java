package util;

import model.DevelopmentCard;
import model.GemType;

public class CardUtil {
    public static String formatCardDetails(DevelopmentCard card) {
        return "[Card ID: " + card.getID() + " | Cost: " + GemType.formatGemMap(card.getCost()) +
            " | Gives: " + card.getBonus().coloredName() + " | Prestige Points: " + card.getPrestigePoints() + "]";
    }
}
