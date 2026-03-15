package game.model;

import game.model.GemType;;
import java.util.*;

// no OOB, invalid input logic 
public class Bank {
    private Map<GemType, Integer> tokens;
    
    public Bank(Map<GemType, Integer> tokens) {
        this.tokens = tokens;
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
