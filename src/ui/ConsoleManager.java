package ui;

import action.*;
import game.*;
import model.*;
import java.util.*;

public class ConsoleManager {
    private InputValidator inputValidator;

    // Constructor
    public ConsoleManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String errorMessage) {
        System.out.println("ERROR: " + errorMessage);
    }

    public int displayWelcomeMenu() {
        System.out.println("\n=================================");
        System.out.println("       WELCOME TO SPLENDOR       ");
        System.out.println("=================================");
        System.out.println("1. Start New Game");
        System.out.println("2. Help / Rules");
        System.out.println("3. Exit");
        
        return inputValidator.getValidInt("Select an option (1-3): ", 1, 3);
    }

    public void displayBoard(GameState gameState) {
        System.out.println("\n=== CURRENT BOARD STATE ===");
        System.out.println("Bank Tokens: " + formatTokenMap(gameState.getBoard().getSupplyTokens()));
        System.out.println("Available Nobles: " + gameState.getBoard().getNobles().size());
        System.out.println("Level 3 Cards: " + gameState.getBoard().getLevel3FaceUp().size() + " visible");
        System.out.println("Level 2 Cards: " + gameState.getBoard().getLevel2FaceUp().size() + " visible");
        System.out.println("Level 1 Cards: " + gameState.getBoard().getLevel1FaceUp().size() + " visible");
        System.out.println("===========================\n");
    }

    public void displayPlayerStatus(Player player) {
        System.out.println("\n--- " + player.getName() + "'s Turn ---");
        System.out.println("Prestige Points: " + player.getPrestigePoints());
        System.out.println("Tokens: " + formatTokenMap(player.getTokens()));
    }

    public void displayHand(Player player) {
        System.out.println("\n--- " + player.getName() + "'s Field ---");
        System.out.println("Reserved Cards: " + player.getReservedCards().size() + "/3");
        System.out.println("Permanent Bonuses: " + formatTokenMap(player.getBonuses()));
        System.out.println("-----------------------------------");
    }

    // utility method to format token maps for clean console printing
    // clean, readable string like "RED: 2, GREEN: 1" instead of raw data
    private String formatTokenMap(Map<GemType, Integer> map) {
        StringBuilder sb = new StringBuilder(); // Sb is faster than + and more memory efficient
        for (Map.Entry<GemType, Integer> entry : map.entrySet()) { // loop through gems and their counts
            if (entry.getValue() > 0) { // if count > 0, print gem
                sb.append(entry.getKey().name()).append(": ").append(entry.getValue()).append(", ");
            }
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 2); // chop off ", " at the end if added entries
        return sb.toString();
    }

    // Displays a list of cards and forces the user to pick a valid one
    public DevelopmentCard selectCard(List<DevelopmentCard> cards) {
        // if no cards, let the fella know and just exit 
        if (cards.isEmpty()) {
            displayMessage("No cards available to select.");
            return null;
        }

        System.out.println("\n--- Select a Card ---");
        // Print the cards with numbers next to them (1, 2, 3...)
        for (int i = 0; i < cards.size(); i++) {
            DevelopmentCard card = cards.get(i);
            // Print format: "1. Card ID: 5 | Cost: RED: 2, BLUE: 1 | Gives: WHITE"
            System.out.println((i + 1) + ". Card ID: " + card.getId() + 
                               " | Cost: " + formatTokenMap(card.getCost()) + 
                               " | Gives: " + card.getBonus().name());
        }

        // Use the InputValidator to get choice
        int choice = inputValidator.getValidInt("Enter the number of your choice: ", 1, cards.size());
        
        // 0-index array, subtract 1 from choice
        return cards.get(choice - 1);
    }

    // Displays available nobles and forces the user to pick one
    public Noble selectNoble(List<Noble> nobleList) {
        // if list empty, move on
        if (nobleList.isEmpty()) {
            return null;
        }

        System.out.println("\n--- Select a Noble ---");
        // loop through nobles and print numbered menu
        for (int i = 0; i < nobleList.size(); i++) {
            Noble noble = nobleList.get(i);
            // Print format: "1. Points: 3 | Requires: RED: 4, WHITE: 4"
            System.out.println((i + 1) + ". Points: " + noble.getPoints() + 
                               " | Requires: " + formatTokenMap(noble.getCost()));
        }

        // get the noble
        int choice = inputValidator.getValidInt("Enter the number of the Noble: ", 1, nobleList.size());
        return nobleList.get(choice - 1);
    }

    // Prompts the user to select up to 3 gems from the bank
    public Map<GemType, Integer> selectGems(Map<GemType, Integer> bankSupply) {
        // create a map to store the result
        Map<GemType, Integer> chosenGems = new java.util.HashMap<>();
        for (GemType type : GemType.values()) {
            chosenGems.put(type, 0); // fill with 0 to prevent errors
        }

        // show user what the bank has in stock
        System.out.println("\n--- Select Gems ---");
        System.out.println("Available in Bank: " + formatTokenMap(bankSupply));
        System.out.println("Note: You can take 3 different tokens, or 2 of the same (if the bank has at least 4).");

        // ask for 3 gems
        int gemsTaken = 0; // tracker
        // keep asking them to take a gem until they hit 3
        while (gemsTaken < 3) {
            System.out.println("\nSelect gem #" + (gemsTaken + 1) + " (or type 0 to stop taking gems):");
            
            // numbered menu for the standard gems
            List<GemType> standardGems = GemType.getStandardGems();
            for (int i = 0; i < standardGems.size(); i++) {
                System.out.println((i + 1) + ". " + standardGems.get(i).name());
            }
            System.out.println("0. Finish taking gems early");

            // get their choice from 0 to 5
            int choice = inputValidator.getValidInt("Your choice: ", 0, standardGems.size());
            
            // if user wants to stop picking gems
            if (choice == 0) {
                break;
            }
            
            // convert their menu choice back to the actual GemType
            // eg if red,blue,white, 1 gets red (0 index)
            GemType selectedType = standardGems.get(choice - 1);
            
            // check if valid: bank has more than enough
            if (bankSupply.getOrDefault(selectedType, 0) - chosenGems.get(selectedType) > 0) {
                chosenGems.put(selectedType, chosenGems.get(selectedType) + 1); // add 1 to player
                gemsTaken++; // increase counter
                System.out.println("Added 1 " + selectedType.name() + " to your hand.");
            } else {
                // out of that colour, pick again
                displayError("The bank does not have enough of that gem left!");
            }
        }
        
        // result
        return chosenGems;
    }
}