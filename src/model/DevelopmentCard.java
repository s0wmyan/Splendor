package model;

import java.util.Map;

public class DevelopmentCard {
    private static int uniqueID = 1;
    private int prestigePoints; // change from prestigepoints -> points
    private int level; // refers to the tier 
    private int id; // purpose of ID? 
    private GemType bonus;
    private Map<GemType, Integer> cost;

    public DevelopmentCard(int level, GemType bonus, Map<GemType, Integer> cost, int prestigePoints) {
        this.level = level;
        this.bonus = bonus;
        this.cost = cost;
        this.prestigePoints = prestigePoints;
        this.id = uniqueID;
        uniqueID++;
    }

    // gettor methods 
    public int getPrestigePoints() {
        return this.prestigePoints;
    }

    public GemType getBonus() {
        return this.bonus;
    }

    public int getLevel() {
        return this.level; 
    }

    public int getID() {
        return this.id;
    }

    public Map<GemType, Integer> getCost() {
        return this.cost;
    }

    // Edited this part
    @Override
    public String toString() {
        return "[Card ID: " + id + " | Cost: " + cost + " | Gives: " + bonus + " | Prestige Points: " + prestigePoints + "]";
    }

}
