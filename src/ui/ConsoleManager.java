package ui;

import game.GameState;
import java.util.*;
import model.DevelopmentCard;
import model.GemType;
import model.Noble;
import model.Player;
import util.CardUtil;

public class ConsoleManager {
    private InputValidator inputValidator;

    // Constructor
    public ConsoleManager(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }


    public void displayHelp() {
        System.out.println("\n=== HELP / QUICK RULES ===");
        System.out.println("- Goal: reach 15 prestige points first (depending on the config).");
        System.out.println("- On your turn choose ONE action:");
        System.out.println("  1) Take tokens: 3 different (e.g. RBK) OR 2 same (bank must have >=4).");
        System.out.println("  2) Buy a card: pay cost using tokens, permanent bonuses reduce cost.");
        System.out.println("  3) Reserve a card: put aside (max 3 reserved. Either reserve a face up card, or randomly reserve from the deck).");
        System.out.println("- Token limit: if you exceed 10 tokens, you must return down to 10.");
        System.out.println("=========================\n");
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
        List<Noble> nobles = gameState.getBoard().getNobles();
        System.out.println("\nNobles on board (" + nobles.size() + "): ");
        System.out.println("===========================");
        for (int i = 0; i < nobles.size(); i++) {
            Noble n = nobles.get(i);
            System.out.println((i + 1) + ". " + n.getName()
                + " | Points: " + n.getPrestigePoints()
                + " | Requires: " + GemType.formatGemMap(n.getCost()));
        }
        System.out.println("===========================\n");

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
            System.out.println(CardUtil.formatCardDetails(card));
        }
    }

    // main method
    public void displayGame(GameState state) {
        sleep(2); // delay so the user can see the confirmation message 
        System.out.print("\033[H\033[2J"); // clear screen 
        displayBoard(state); 
        displayAllPlayersState(state); // Player details 
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }

    public void displayAllPlayersState(GameState state) {
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
        System.out.println("Permanent Bonuses: " + GemType.formatGemMap(player.getBonuses())); 
        System.out.println("Reserved Cards: " + player.getReservedCards().size() + "/3"); 
        displayCard(player.getReservedCards());
        System.out.println("Purchased Cards: "); 
        displayCard(player.getPurchasedCards());

        // cards in hand? 
    }

    public int promptForPlayerCount() {
        return inputValidator.getValidInt("Enter Player Count (2/4) : ", 2, 4);
    }

    public int promptReserveType() {
        System.out.println("(1): Reserve a face-up card");
        System.out.println("(2): Blind reserve from a deck (Tier 1-3)");
        System.out.println("");
        return inputValidator.getValidInt("Select reserve type (1-2): ", 1, 2);
    }

    public int promptBlindReserveTier() {
        return inputValidator.getValidInt("Select tier to blind reserve (1-3): ", 1, 3);
    }

    // "name": "dd/mm/yyyy" format 
    // Can't have the same name, else it will override. 
    
    
    public Map<String, String> promptForPlayerDetails(int playerCount) {
        Map<String, String> playerDetails = new LinkedHashMap<>();

        for (int i = 1; i <= playerCount; i++) {
            String name;
            while (true) {
                name = inputValidator.getValidString(String.format("Enter Player %d's name : ", i));
                if (!playerDetails.containsKey(name)) {
                    break;
                }
                System.out.println("This name is already taken. Please enter a unique name.");
            }

            String birthday = inputValidator.getValidBirthday(
                String.format("Enter Player %d's birthday (DD/MM/YYYY) : ", i));
            playerDetails.put(name, birthday);
        }

        return playerDetails;
    }


    // Displays a list of cards 
    public DevelopmentCard selectCard(Player player, List<DevelopmentCard> boardCards, List<DevelopmentCard> reservedCards, boolean selectReserved) {
        // if no cards, let the fella know and just exit 
        if (boardCards.isEmpty() && reservedCards.isEmpty()) {
            displayMessage("No cards available to select.");
            return null;
        }
        if (selectReserved) {
            System.out.println("\n--- Reserve a Card ---");
        } else {
            System.out.println("\n--- Select a Card ---");
        }

        // Print the cards with numbers next to them (1, 2, 3...)
        List<DevelopmentCard> allCardsList = new ArrayList<>();

        for (int i = 0; i < boardCards.size(); i++) {
            DevelopmentCard card = boardCards.get(i);
            // Print format: "1. Card ID: 5 | Cost: RED: 2, BLUE: 1 | Gives: WHITE"
            System.out.println((i + 1) + ". " + CardUtil.formatCardDetails(card)); // added card id
            allCardsList.add(boardCards.get(i));
        }
        System.out.println();

        if (!reservedCards.isEmpty()) {
            System.out.println("\n--- Your reserved cards ---");
            for (int i = 0; i < reservedCards.size(); i++) {
                System.out.println((boardCards.size() + i + 1) + ". " + CardUtil.formatCardDetails(reservedCards.get(i)));
                allCardsList.add(reservedCards.get(i));
            }
        }

        System.out.println("0. Return to menu");
        
        // Use the InputValidator to get choice
        int choice = inputValidator.getValidInt("Enter the number of your choice: ", 0, allCardsList.size());

        if (choice == 0) {
            System.out.println("");
            System.out.println("** Returning to menu **\n");
            return null;
        }
        
        // what choice selected ?
        // method to parse card into ( Card ID: 5 | Cost: RED: 2, BLUE: 1 | Gives: WHITE ) string. 
        DevelopmentCard cardSelected = allCardsList.get(choice - 1);
        System.out.println("Choice " + choice + " selected!");
        // System.out.println("Player " + player.getName() + " has reserved the card: " + CardUtil.formatCardDetails(cardSelected)); => shift to reservecardaction 
        
        // 0-index array, subtract 1 from choice from allCardsList
        return cardSelected;
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

    public DevelopmentCard selectCard(Player player, List<DevelopmentCard> boardCards) {
        return selectCard(player, boardCards, new ArrayList<>(), true);
    }

    // alternative to selectgems
    public Map<GemType, Integer> selectGems(Map<GemType, Integer> bankSupply) {
        // show user what the bank has in stock
        System.out.println("\n--- Select Gems ---");
        System.out.println("Available in Bank: " + GemType.formatGemMap(bankSupply));
        System.out.println("\nNote: You can take 3 different tokens, or 2 of the same token (if the bank has at least 4 of that token.).");
        
        System.out.println("\nLegend:");
        System.out.println("  " + GemType.RED.coloredChar() + " = " + GemType.RED.coloredName());
        System.out.println("  " + GemType.BLUE.coloredChar() + " = " + GemType.BLUE.coloredName());
        System.out.println("  " + GemType.WHITE.coloredChar() + " = " + GemType.WHITE.coloredName());
        System.out.println("  " + GemType.GREEN.coloredChar() + " = " + GemType.GREEN.coloredName());
        System.out.println("  " + GemType.BLACK.coloredChar() + " = " + GemType.BLACK.coloredName());
        System.out.println(
            "Example: Type 'RBK'"
                + " to take 1 "
                + GemType.RED.coloredName()
                + ", 1 "
                + GemType.BLUE.coloredName()
                + ", 1 "
                + GemType.BLACK.coloredName()
                + " token");
        System.out.println("Type 0 to cancel.");
        String input = inputValidator.getValidString("Enter your choice: ");
        if (input.equals("0")) return null;

        while (!validSelectGemsInput(input)) {
            displayError("Invalid choice. Use 2 or 3 letters from {R,B,W,G,K} (example: RBK).");
            input = inputValidator.getValidString("Enter your choice (or 0 to cancel): ");
            if (input.equals("0")) return null;
        }

        Map<GemType, Integer> chosenGems = new HashMap<>();
        input = input.trim().toUpperCase();
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
        for (char c : input.toCharArray()) {
            if (!validCharacters.contains(c)) return false;
        }
        return true;
    }

    public boolean birthdayValidator(String input) {
        return inputValidator.isValidBirthdayDDMMYYYY(input);
    }

    // prompt for the game mode, vs computers or humans?
    public int promptForGameMode() {
        System.out.println("\n=== Select game mode ===");
        System.out.println("1. Party (vs Humans)");
        System.out.println("2. Solo (vs Computers)");

        return inputValidator.getValidInt("Select mode (1-2): ", 1, 2);
    }

    // Print post match analytics
    public void printGameStats(List<Player> players, Player winner) {
        System.out.println("\n=======================================================================");
        System.out.println("                         POST-MATCH ANALYTICS                          ");
        System.out.println("=======================================================================");

        System.out.println("🏅 Winner: " + winner.getName() + " 🏅\n");

        // Header
        System.out.printf("%-15s | %-10s | %-12s | %-14s | %-10s%n", 
                        "Player", "Prestige", "Cards Bought", "Gems Collected", "Turns");
        System.out.println("-----------------------------------------------------------------------");

        // Sort player by points
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.getPrestigePoints(), p1.getPrestigePoints()));

        for (Player p : sortedPlayers) {
            System.out.printf("%-15s | %-10d | %-12d | %-14d | %-10d%n", 
                            p.getName(), p.getPrestigePoints(), p.getCardsPurchased(), 
                            p.getTotalGemsCollected(), p.getTurnsTaken());
        }
        System.out.println("=======================================================================\n");
    }
    
}



