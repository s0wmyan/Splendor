package Game.Model;

import java.util.Map;

public class DevelopmentCard {
    private int points; // change from prestigepoints -> points
    private int level; // refers to the tier 
    private int id; // purpose of ID? 
    private GemType bonus;
    private Map<GemType, Integer> cost;

    public DevelopmentCard(int points, int level, GemType bonus, Map<GemType, Integer> cost) {
        this.points = points;
        this.level = level;
        this.bonus = bonus;
        this.cost = cost;
    
    }

    // gettor methods 
    public int getPoints() {
        return this.points;
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
}
