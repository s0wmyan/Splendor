package model;

import java.util.List;
import java.util.Map;

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

    // utility method to format token maps for clean console printing
    // clean, readable string like "RED: 2, GREEN: 1" instead of raw data
    public static String formatGemMap(Map<GemType, Integer> map) {
        StringBuilder sb = new StringBuilder(); // Sb is faster than + and more memory efficient
        for (Map.Entry<GemType, Integer> entry : map.entrySet()) { // loop through gems and their counts
            if (entry.getValue() > 0) { // if count > 0, print gem
                sb.append(entry.getKey().name()).append(": ").append(entry.getValue()).append(", ");
            }
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 2); // chop off ", " at the end if added entries
        return sb.toString();
    }
}
