package model;

public enum GemType {
    RED, 
    BLUE, 
    WHITE, 
    GREEN, 
    BLACK, 
    YELLOW; // yellow == gold, rest are normal

    public boolean isStandard() {
        return this != YELLOW;
    }
}
