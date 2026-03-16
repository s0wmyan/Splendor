package model;

import java.util.*;
import model.GemType;


public class Noble {
    private String name;
    private Map<GemType, Integer> cost;
    private int points;

    public Noble(String name, Map<GemType, Integer> cost, int points) {
        this.name = name;
        this.cost = cost;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public Map<GemType, Integer> getCost() {
        return cost;
    }

    public int getPoints() {
        return points;
    }


}
