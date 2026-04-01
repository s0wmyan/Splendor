package util;
import java.io.*;
import java.util.*;
import model.*;

public class DataLoader {
    public static List<DevelopmentCard> loadCards(String filePath) {
        List<DevelopmentCard> cards = new ArrayList<>();
        
        try (Scanner fileSc = new Scanner(new File(filePath))) {
            
            fileSc.nextLine();

            while  (fileSc.hasNext()) {
                String cardData = fileSc.nextLine();
                
                String[] data = cardData.split(",");

                int level = Integer.parseInt(data[0].trim());
                GemType bonus = GemType.valueOf(data[1].trim().toUpperCase());
                int points = Integer.parseInt(data[2].trim());
                

                Map<GemType, Integer> cost = new HashMap<>();
                
                int blackCost = Integer.parseInt(data[3].trim());
                if (blackCost > 0) cost.put(GemType.BLACK, blackCost);
                
                int blueCost = Integer.parseInt(data[4].trim());
                if (blueCost > 0) cost.put(GemType.BLUE, blueCost);
                
                int greenCost = Integer.parseInt(data[5].trim());
                if (greenCost > 0) cost.put(GemType.GREEN, greenCost);
                
                int redCost = Integer.parseInt(data[6].trim());
                if (redCost > 0) cost.put(GemType.RED, redCost);
                
                int whiteCost = Integer.parseInt(data[7].trim());
                if (whiteCost > 0) cost.put(GemType.WHITE, whiteCost);

                cards.add(new DevelopmentCard(level, bonus, cost, points));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cards;
    }

    public static List<Noble> loadNobles(String filePath) {
        List<Noble> nobles = new ArrayList<>();

        try (Scanner fileSc = new Scanner(new File(filePath))) {
            
            fileSc.nextLine();

            while  (fileSc.hasNext()) {
                String cardData = fileSc.nextLine();
                
                String[] data = cardData.split(",");

                String name = data[0].trim();
                int points = Integer.parseInt(data[1].trim());
                

                Map<GemType, Integer> cost = new HashMap<>();
                
                int blackCost = Integer.parseInt(data[2].trim());
                if (blackCost > 0) cost.put(GemType.BLACK, blackCost);
                
                int blueCost = Integer.parseInt(data[3].trim());
                if (blueCost > 0) cost.put(GemType.BLUE, blueCost);
                
                int greenCost = Integer.parseInt(data[4].trim());
                if (greenCost > 0) cost.put(GemType.GREEN, greenCost);
                
                int redCost = Integer.parseInt(data[5].trim());
                if (redCost > 0) cost.put(GemType.RED, redCost);
                
                int whiteCost = Integer.parseInt(data[6].trim());
                if (whiteCost > 0) cost.put(GemType.WHITE, whiteCost);

                nobles.add(new Noble(name, cost, points));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nobles;
    }
}