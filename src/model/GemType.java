package model;

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
}
