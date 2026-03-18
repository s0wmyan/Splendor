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
        System.out.println(">>> ERROR: " + errorMessage);
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

        // player count
    public int promptForPlayerCount() {
        // between 2 and 4
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of players : ");
        int playerCount = sc.nextInt();

        return playerCount;
    }

    // player names
    /*
    Map<String, String> playerDetail = promptForPlayerDetails(playerCount)
    k -> name, v -> birthday as a string 
    */
    public Map<String, String> promptForPlayerDetails(int playerCount) {
        // birthday and name
        // name needs to be unique 
        // birthday as a string -> need to be specific? 

        Scanner sc = new Scanner(System.in);
        Map<String, String> playerDetails = new HashMap<>();

        for (int i = 1; i <= playerCount; i++) {
            System.out.println("Enter Player " + i + "'s name : ");
            String name = sc.nextLine();

            System.out.println("Enter Player " + i + "'s birthday (DD/MM/YY) : ");
            String birthday = sc.nextLine(); // no validating 

            playerDetails.put(name, birthday);
        }
        
        return playerDetails;
    }

    // Prompts the player for their move and returns the corresponding Action object
    public Action promptForAction(Player player) {
        System.out.println("\nAvailable Actions:");
        System.out.println("1. Take Gems");
        System.out.println("2. Buy a Card");
        System.out.println("3. Reserve a Card");

        int choice = inputValidator.getValidInt("What would you like to do? (1-3): ", 1, 3);

        // switch (choice) {
        //     case 1:
        //         return promptTakeGems();
        //     case 2:
        //         return promptBuyCard();
        //     case 3:
        //         return promptReserveCard();
        //     default:
        //         return promptForAction(player); 
        // }
        return null;
    }

    // --- Private Helper Methods ---
    // promptTakeGems
    // promptBuyCard
    // prompt reserveCard
    // prompt formatTokenMap

    // utility method to format token maps for clean console printing.
    private String formatTokenMap(Map<GemType, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<GemType, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 0) {
                sb.append(entry.getKey().name()).append(": ").append(entry.getValue()).append(", ");
            }
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    // Displays a list of cards and forces the user to pick a valid one.
    public DevelopmentCard selectCard(List<DevelopmentCard> cards) {
        if (cards.isEmpty()) {
            displayMessage("No cards available to select.");
            return null;
        }

        System.out.println("\n--- Select a Card ---");
        // Print the cards with numbers next to them (1, 2, 3...)
        for (int i = 0; i < cards.size(); i++) {
            DevelopmentCard card = cards.get(i);
            System.out.println((i + 1) + ". Card ID: " + card.getId() + 
                               " | Cost: " + formatTokenMap(card.getCost()) + 
                               " | Gives: " + card.getBonus().name());
        }

        // Use the InputValidator to get choice
        int choice = inputValidator.getValidInt("Enter the number of your choice: ", 1, cards.size());
        
        // 0-index array, subtract 1 from choice
        return cards.get(choice - 1);
    }

    // Displays available nobles and forces the user to pick one. 
    public Noble selectNoble(List<Noble> nobleList) {
        if (nobleList.isEmpty()) {
            return null;
        }

        System.out.println("\n--- Select a Noble to Visit You ---");
        for (int i = 0; i < nobleList.size(); i++) {
            Noble noble = nobleList.get(i);
            System.out.println((i + 1) + ". Points: " + noble.getPrestigePoints() + 
                               " | Requires: " + formatTokenMap(noble.getCost()));
        }

        int choice = inputValidator.getValidInt("Enter the number of the Noble: ", 1, nobleList.size());
        return nobleList.get(choice - 1);
    }

    // Prompts the user to select gems from the bank
    public Map<GemType, Integer> selectGems(Map<GemType, Integer> bankSupply) {
        // We use a Map to store what the user decides to take
        Map<GemType, Integer> chosenGems = new java.util.HashMap<>();
        for (GemType type : GemType.values()) {
            chosenGems.put(type, 0);
        }

        System.out.println("\n--- Select Gems ---");
        System.out.println("Available in Bank: " + formatTokenMap(bankSupply));
        System.out.println("Note: You can take 3 different tokens, or 2 of the same (if the bank has at least 4).");

        // ask for 3 gems
        int gemsTaken = 0;
        while (gemsTaken < 3) {
            System.out.println("\nSelect gem #" + (gemsTaken + 1) + " (or type 0 to stop taking gems):");
            
            // numbered menu for the standard gems
            List<GemType> standardGems = GemType.getStandardGems();
            for (int i = 0; i < standardGems.size(); i++) {
                System.out.println((i + 1) + ". " + standardGems.get(i).name());
            }
            System.out.println("0. Finish taking gems early");

            int choice = inputValidator.getValidInt("Your choice: ", 0, standardGems.size());
            
            if (choice == 0) {
                break; // if user wants to stop picking gems
            }
            
            GemType selectedType = standardGems.get(choice - 1);
            
            // check if bank actually has it
            if (bankSupply.getOrDefault(selectedType, 0) - chosenGems.get(selectedType) > 0) {
                chosenGems.put(selectedType, chosenGems.get(selectedType) + 1);
                gemsTaken++;
                System.out.println("Added 1 " + selectedType.name() + " to your hand.");
            } else {
                displayError("The bank does not have enough of that gem left!");
            }
        }
        
        return chosenGems;
    }
}