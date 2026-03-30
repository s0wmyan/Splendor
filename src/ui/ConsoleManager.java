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
        System.out.println("\n=== CURRENT BOARD STATE ===\n");
        System.out.println("Bank Tokens: " + GemType.formatGemMap(gameState.getBoard().getBank().getTokens()));
        System.out.println("Available Nobles: " + gameState.getBoard().getNobles().size());
        // System.out.println("Level 3 Cards: " + gameState.getBoard().getLevel3FaceUp().size() + " visible");
        // System.out.println("Level 2 Cards: " + gameState.getBoard().getLevel2FaceUp().size() + " visible");
        // System.out.println("Level 1 Cards: " + gameState.getBoard().getLevel1FaceUp().size() + " visible");
        System.out.println("\nLevel 3 Cards: ");
        System.out.println("===========================");
        displayCard(gameState.getBoard().getLevel3FaceUp());
        System.out.println("===========================\n");

        System.out.println("Level 2 Cards: ");
        System.out.println("===========================");
        displayCard(gameState.getBoard().getLevel2FaceUp());
        System.out.println("===========================\n");

        System.out.println("Level 1 Cards: ");
        System.out.println("===========================");
        displayCard(gameState.getBoard().getLevel1FaceUp());
        System.out.println("===========================\n");
    }

    // displays a deck of cards with the following format: 
    // "1. Card ID: 5 | Cost: RED: 2, BLUE: 1 | Gives: WHITE"
    public void displayCard(List<DevelopmentCard> deck) {
        for (DevelopmentCard card : deck) {
            System.out.println(formatCardDetails(card));
        }
    }

    // main method
    public void displayGame(GameState state) {
        System.out.print("\033[H\033[2J"); // clear screen 

        displayBoard(state); 
        displayGameState(state); // Player details 
    }

    public void displayPlayerStatus(Player player) {
        System.out.println("\n--- " + player.getName() + "'s Turn ---");
        System.out.println("Prestige Points: " + player.getPrestigePoints());
        System.out.println("Tokens: " + GemType.formatGemMap(player.getTokens()));
        displayHand(player);
    }


    public void displayGameState(GameState state) {
        // displays all players state
        System.out.println("=== CURRENT PLAYERS STATE ===");
        for (Player player : state.getPlayers()) {
            displayPlayerState(player);
        }
        System.out.println("-----------------------------------");
    }

    private void displayPlayerState(Player player) {
        System.out.println("\n--- " + player.getName() + " ---");
        System.out.println("Prestige Points: " + player.getPrestigePoints());
        System.out.println("Tokens: " + GemType.formatGemMap(player.getTokens()));
        // reserved cards, permanent bonuses, cards in hand? 
        System.out.println("Reserved Cards: " + player.getReservedCards().size() + "/3"); // only the size?? 
        System.out.println("Permanent Bonuses: " + GemType.formatGemMap(player.getBonuses())); // is this even working?
        System.out.println("Purchased Cards: "); 
        displayCard(player.getPurchasedCards());

        // cards in hand? 
    }


    public void displayHand(Player player) {
        // System.out.println("\n--- " + player.getName() + "'s Field ---");
        System.out.println("Reserved Cards: " + player.getReservedCards().size() + "/3");
        System.out.println("Permanent Bonuses: " + GemType.formatGemMap(player.getBonuses()));
        System.out.println("-----------------------------------");
    }


    public int promptForPlayerCount() {
        return inputValidator.getValidInt("Enter Player Count (2/4) : ", 2, 4);
    }

    // "name": "dd/mm/yyyy" format 
    // Can't have the same name, else it will override. 
    public Map<String, String> promptForPlayerDetails(int playerCount) {
        Map<String, String> playerDetails = new HashMap<>();

        for (int i = 1; i <= playerCount; i++) {
            String name = inputValidator.getValidString(String.format("Enter Player %d's name : ", i));
            String birthday = inputValidator.getValidString(String.format("Enter Player %d's birthday (DD/MM/YYYY) : ", i));
            playerDetails.put(name, birthday); // cannot be the same name 
        }

        return playerDetails;
    }

    

    private String formatCardDetails(DevelopmentCard card) {
        return "[Card ID: " + card.getID() + " | Cost: " + GemType.formatGemMap(card.getCost()) + 
            " | Gives: " + card.getBonus().name() + "]";
    }

    // Displays a list of cards and forces the user to pick a valid one
    public DevelopmentCard selectCard(Player player, List<DevelopmentCard> cards) {
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
            System.out.println((i + 1) + ". " + formatCardDetails(card)); // added card id
        }
        System.out.println();

        // Use the InputValidator to get choice
        int choice = inputValidator.getValidInt("Enter the number of your choice: ", 1, cards.size());
        // what choice selected ?
        // method to parse card into ( Card ID: 5 | Cost: RED: 2, BLUE: 1 | Gives: WHITE ) string. 
        DevelopmentCard cardSelected = cards.get(choice - 1);
        System.out.println("Choice " + choice + " selected!");
        System.out.println(player.getName() + " selected card: " + formatCardDetails(cardSelected));
        
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
            System.out.println((i + 1) + ". Points: " + noble.getPrestigePoints() + 
                               " | Requires: " + GemType.formatGemMap(noble.getCost()));
        }

        // get the noble
        int choice = inputValidator.getValidInt("Enter the number of the Noble: ", 1, nobleList.size());
        return nobleList.get(choice - 1);
    }

    // alternative to selectgems
    public Map<GemType, Integer> selectGems(Map<GemType, Integer> bankSupply) {
        System.out.println("\n--- Select Gems ---");
        System.out.println("Available in Bank: " + GemType.formatGemMap(bankSupply));
        System.out.println("Note: You can take 3 different tokens, or 2 of the same (if the bank has at least 4).");
        
        System.out.println("\nLegend: [R : Red, B - Blue, W - White, G - Green, K - Black] (e.g. Type 'RBK' to take 1 Red, 1 Blue, 1 Black token)");
        String input = inputValidator.getValidString("Enter your choice : ");

        while (!validSelectGemsInput(input)) {
            System.out.println("(Console Manager Line 207): Invalid Input!");
            System.out.println("\nLegend: [R : Red, B - Blue, W - White, G - Green, K - Black] (e.g. Type 'RBK' to take 1 Red, 1 Blue, 1 Black token)");
            input = inputValidator.getValidString("Enter your choice :");
        }

        Map<GemType, Integer> chosenGems = new HashMap<>();
        for (char c : input.toCharArray()) {
            GemType gemSelected = GemType.fromChar(c);
            chosenGems.put(gemSelected, chosenGems.getOrDefault(gemSelected, 0) + 1);
        }

        // if simple check says invalid -> skip 
        return chosenGems; // return and leave it to action.validate to work its magic. 
    }

    private boolean validSelectGemsInput(String input) {
        if (input.length() != 3 && input.length() != 2) {
            return false;
        }


        Set<Character> validCharacters = new HashSet<>(Arrays.asList('R', 'B', 'W', 'G', 'K'));
        input = input.toUpperCase(); // case insensitive

        for (char c : input.toCharArray()) {

            if (validCharacters.contains(c)) {
                return true;
            }
        }

        return false;
    }

    // Prompts the user to select up to 3 gems from the bank
    // public Map<GemType, Integer> selectGems(Map<GemType, Integer> bankSupply) {
    //     Map<GemType, Integer> chosenGems = new HashMap<>();
    //     for (GemType type : GemType.values()) {
    //         chosenGems.put(type, 0); // fill with 0 to prevent errors
    //     }

    //     // show user what the bank has in stock
    //     System.out.println("\n--- Select Gems ---");
    //     System.out.println("Available in Bank: " + formatTokenMap(bankSupply));
    //     System.out.println("Note: You can take 3 different tokens, or 2 of the same (if the bank has at least 4).");

    //     int gemCount = 0;
    //     Map<GemType, Integer> gemsTaken = new HashMap<>();
    //     gemCount = 0;
    //     while (gemCount < 3) {
    //         System.out.println("\nSelect gem #" + (gemCount + 1) + ":");
            
    //         // numbered menu for the standard gems
    //         List<GemType> standardGems = GemType.getStandardGems();
    //         for (int i = 0; i < standardGems.size(); i++) {
    //             System.out.println((i + 1) + ". " + standardGems.get(i).name());
    //         }
    //         int choice = inputValidator.getValidInt("Your choice: ", 1, standardGems.size());
    //         GemType gemSelected = standardGems.get(choice - 1);

    //         // first check: bank supply enough? 
    //         if (bankSupply.get(gemSelected) <  gemsTaken.getOrDefault(gemSelected, 0) + 1) {
    //             System.out.println("Invalid Move: The bank does not have enough of that gem left!");
    //             System.out.println();
    //             continue; 
    //         }

    //         // second check: if player takes 3 tokens, with two of the same kind. 
    //         if (gemsTaken.containsKey(gemSelected)) {
    //             // selected before, will be 2
    //             if (gemCount == 2) {
    //                 System.out.println("Invalid Move: You can take 3 different tokens, or 2 of the same (if the bank has at least 4)");
    //                 continue; 
    //             } else {
    //                 gemsTaken.put(gemSelected, gemsTaken.get(gemSelected) + 1);
    //                 System.out.println("Added 1 " + gemSelected.name() + " to your hand.");
    //                 break; // exit (2 is enough - time to check the move)
    //             }
    //         }


    //         gemsTaken.put(gemSelected, 1);
    //         System.out.println("Added 1 " + gemSelected.name() + " to your hand.");
    //         gemCount++;
    //     }    
        
    //     return gemsTaken; // app will check if it is valid? 
    // }



        // flow

        // get choice -> add token to map  -> if it contains 
    // OLD METHOD - TO BE DELETED

    // // Prompts the user to select up to 3 gems from the bank
    // public Map<GemType, Integer> selectGems(Map<GemType, Integer> bankSupply) {
    //     // create a map to store the result
    //     Map<GemType, Integer> chosenGems = new HashMap<>();
    //     for (GemType type : GemType.values()) {
    //         chosenGems.put(type, 0); // fill with 0 to prevent errors
    //     }

    //     // show user what the bank has in stock
    //     System.out.println("\n--- Select Gems ---");
    //     System.out.println("Available in Bank: " + formatTokenMap(bankSupply));
    //     System.out.println("Note: You can take 3 different tokens, or 2 of the same (if the bank has at least 4).");

    //     // ask for 3 gems
    //     int gemsTaken = 0; // tracker
    //     // keep asking them to take a gem until they hit 3
    //     while (gemsTaken < 3) {
    //         System.out.println("\nSelect gem #" + (gemsTaken + 1) + " (or type 0 to stop taking gems):");
            
    //         // numbered menu for the standard gems
    //         List<GemType> standardGems = GemType.getStandardGems();
    //         for (int i = 0; i < standardGems.size(); i++) {
    //             System.out.println((i + 1) + ". " + standardGems.get(i).name());
    //         }
    //         System.out.println("0. Finish taking gems early");

    //         // get their choice from 0 to 5
    //         int choice = inputValidator.getValidInt("Your choice: ", 0, standardGems.size());
            
    //         // if user wants to stop picking gems
    //         if (choice == 0) {
    //             break;
    //         }
            
    //         // convert their menu choice back to the actual GemType
    //         // eg if red,blue,white, 1 gets red (0 index)
    //         GemType selectedType = standardGems.get(choice - 1);
            
    //         // check if valid: bank has more than enough
    //         if (bankSupply.getOrDefault(selectedType, 0) - chosenGems.get(selectedType) > 0) {
    //             chosenGems.put(selectedType, chosenGems.get(selectedType) + 1); // add 1 to player
    //             gemsTaken++; // increase counter
    //             System.out.println("Added 1 " + selectedType.name() + " to your hand.");
    //         } else {
    //             // out of that colour, pick again
    //             displayError("The bank does not have enough of that gem left!");
    //         }
    //     }
        

    // birthdayValidator
    public boolean birthdayValidator(String input) {
        if (input == null || input.length() != 10 || input.charAt(2) != '/' || input.charAt(5) != '/') {
            return false;
        }

        String[] array = input.split("/");

        try {
            int day = Integer.parseInt(array[0]);
            int month = Integer.parseInt(array[1]);
            int year = Integer.parseInt(array[2]);

            if (month < 1 || month > 12) {
                return false;
            }

            int maxDays = 31;

            if (month == 4 || month ==  6|| month == 9 || month == 1) {
                maxDays = 30;
            } else if (month == 2) {
                maxDays = 28;
            }

            if (day < 1 || day > maxDays) {
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}



