package model;

import java.util.List;

public enum GemType {
    RED, 
    BLUE, 
    WHITE, 
    GREEN, 
    BLACK, 
    GOLD; // gold

    public boolean isStandard() {
        return this != GOLD;
    }

    public static List<GemType> getStandardGems() {
        return List.of(RED, BLUE, WHITE, GREEN, BLACK);
    }
}
