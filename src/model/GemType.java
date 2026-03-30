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

    public static GemType fromChar(char c) {
        switch (c) {
            case 'R': return RED;
            case 'B': return BLUE;
            case 'W': return WHITE;
            case 'G': return GREEN;
            case 'K': return BLACK;
            default: return null;
        }
    }
}
