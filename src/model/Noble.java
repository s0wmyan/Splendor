package model;

import java.util.*;
import model.GemType;


public class Noble {
    private String name;
    private Map<GemType, Integer> cost;
    private int prestigePoints;

    public Noble(String name, Map<GemType, Integer> cost, int prestigePoints) {
        this.name = name;
        this.cost = cost;
        this.prestigePoints = prestigePoints;
    }

    public String getName() {
        return name;
    }

    public Map<GemType, Integer> getCost() {
        return cost;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }


}
