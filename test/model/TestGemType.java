package test.model;

import model.GemType;
import java.util.*;

public class TestGemType {
    public static void main(String[] args) {
        
        List<GemType> gems = GemType.getStandardGems();
        System.out.println(gems.toString());

    }    
}
