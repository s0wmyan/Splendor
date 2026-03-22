package model;

import java.util.EnumMap;
import java.util.Map;

// no OOB, invalid input logic 
public class Bank {
    private Map<GemType, Integer> tokens = new EnumMap<>(GemType.class);

    public Bank() {
        for (GemType type : GemType.values()) {
            tokens.put(type, 0);
        }
    }
    
    public Bank(Map<GemType, Integer> tokens) {
        this();
        if (tokens != null) this.tokens = tokens;
    }

    public int getTokenCount(GemType type) {
        return tokens.getOrDefault(type, -1);

        // returns -1 if not present
    }

    public void setTokenCount(GemType type, int count) {
        tokens.put(type, count); 

        // set should be enough already
    }

    public void addTokenCount(GemType type, int count) {
        setTokenCount(type, getTokenCount(type) + count);
    }

    public void removeTokenCount(GemType type, int count) {
        setTokenCount(type, getTokenCount(type) - count);
    }
}
