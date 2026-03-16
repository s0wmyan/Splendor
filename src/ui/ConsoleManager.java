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
}