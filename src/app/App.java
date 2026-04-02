package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;

import engine.TurnManager;
import engine.WinConditionChecker;
import game.Game;
import game.GameState;
import model.Bank;
import model.Birthday;
import model.Board;
import model.Deck;
import model.DevelopmentCard;
import model.GemType;
import model.HumanPlayer;
import model.ComputerPlayer;
import model.Noble;
import model.Player;
import ui.ConsoleManager;
import ui.InputValidator;
import util.ConfigLoader;
import util.DataLoader;


public class App {

    private static void seedBank(Bank bank, int playerCount, ConfigLoader config) {
        int standardGemCount = switch (playerCount) {
            case 2 -> config.getIntProperty("gems2Players", 4);
            case 3 -> config.getIntProperty("gems3Players", 5);
            case 4 -> config.getIntProperty("gems4Players", 7);
            default -> 7;
        };
        for (GemType type : GemType.getStandardGems()) {
            bank.setTokenCount(type, standardGemCount);
        }

        int goldCount = config.getIntProperty("goldCount", 5);
        bank.setTokenCount(GemType.GOLD, goldCount);
    }

    private static Deck buildDeck(int level, ConfigLoader config) {

        String filePath = switch (level) {
            case 1 -> config.getFilePath("level1Cards");
            case 2 -> config.getFilePath("level2Cards");
            case 3 -> config.getFilePath("level3Cards");
            default -> null;
        };

        List<DevelopmentCard> cards = DataLoader.loadCards(filePath);
        Deck deck = new Deck(level, cards);
        deck.shuffle();
        return deck;
    }

    private static List<Noble> buildNobles(int playerCount, ConfigLoader config) {
        String filePath = config.getFilePath("nobles");
        List<Noble> nobles = DataLoader.loadNobles(filePath);  
        Collections.shuffle(nobles);

        int removeCount = nobles.size() - playerCount - 1;
        for (int i = 0; i < removeCount; i++) {
            nobles.remove(nobles.size() - 1);
        }

        return nobles;
    }

    public static void main(String[] args) {
        // initialization
        Scanner sc = new Scanner(System.in);
        InputValidator inputValidator = new InputValidator(sc);
        ConsoleManager cManager = new ConsoleManager(inputValidator);
        ConfigLoader config = new ConfigLoader("config.properties");

        // check gamemode and no of players
        int gameMode = cManager.promptForGameMode();
        int playerCount = cManager.promptForPlayerCount();
        System.out.println("No. of players: " + playerCount);

        // Gather data for re-use
        Map<String, String> mode1Details = null;
        Map<String, String> mode2Details = null;

        if (gameMode == 1) {
            mode1Details = cManager.promptForPlayerDetails(playerCount);
        } else {
            System.out.println("\nEnter your details ");
            mode2Details = cManager.promptForPlayerDetails(1);
        }

        // Game loop
        boolean playAgain = true;

        while (playAgain) {
            System.out.println("\n--- SETTING UP THE BOARD ---");

            List<Player> players = new ArrayList<>();
            int firstPlayerIndex = 0;

            // Generate fresh players so that inventories are clean
            // mode 1: human vs humans
            if (gameMode == 1) {
                // initialize the humans
                for (Map.Entry<String, String> entry : mode1Details.entrySet()) {
                    String playerName = entry.getKey();
                    String bday = entry.getValue();

                    int day = Integer.parseInt(bday.substring(0, 2));
                    int month = Integer.parseInt(bday.substring(3, 5));
                    int year = Integer.parseInt(bday.substring(6, 10));

                    players.add(new HumanPlayer(playerName, new Birthday(day, month, year)));
                }

                // sort the list of players, starting w youngest
                players.sort(
                    Comparator.comparing(Player::getBirthday)
                    .reversed()                         // youngest first
                    .thenComparing(Player::getName));

                // print turn order
                System.out.println("\nOffical turn order");
                for (int i = 0; i < players.size(); i++) {
                    System.out.println("Player " + (i + 1) + " - " + players.get(i).toString());
                }

                // youngest player -> firstPlayerIndex = 0;
            } else { // mode 2: human vs Computers
                // enter human details and initialize as first player
                Map.Entry<String, String> entry = mode2Details.entrySet().iterator().next();

                String bday = entry.getValue();
                int day = Integer.parseInt(bday.substring(0, 2));
                int month = Integer.parseInt(bday.substring(3, 5));
                int year = Integer.parseInt(bday.substring(6, 10));

                players.add(new HumanPlayer(entry.getKey(), new Birthday(day, month, year)));

                // auto generate the computer players
                for (int i = 1; i < playerCount; i++) {
                    players.add(new ComputerPlayer("Bot " + i));
                }

                // shuffle list to randomize
                Collections.shuffle(players);
                firstPlayerIndex = 0; // first player is random

                // print turn order
                System.out.println("\nOffical turn order");
                for (int i = 0; i < players.size(); i++) {
                    System.out.println("Player " + (i + 1) + " - " + players.get(i).getName()); // dont use toString here as it will print out the bogus birthday as well
                }
            }

            // build board 
            Board board = new Board(buildDeck(1, config), buildDeck(2, config), buildDeck(3, config), buildNobles(playerCount, config));
            seedBank(board.getBank(), playerCount, config);
            board.refillAll();

            // setup win condition from config 
            int winningPrestigeRequirement = config.getIntProperty("winningConditionPoints", 15);
            WinConditionChecker winConditionChecker = new WinConditionChecker(winningPrestigeRequirement);

            TurnManager turnManager = new TurnManager(inputValidator, cManager, winConditionChecker, playerCount); 

            GameState gameState = new GameState(board, players);
            Game game = new Game(gameState, turnManager);

            gameState.setPlayerIndex(firstPlayerIndex);

            // run the game 
            System.out.println("Game is Starting !");
            game.run(); 

            Player winner = gameState.getCurrentPlayer();

            // print post match stats
            cManager.printGameStats(gameState.getPlayers(), winner);

            // re-prompt message
            System.out.println("\nWould you like to play again with the same players?");
            System.out.println("1. Yes, rematch");
            System.out.println("2. No, exit");

            int choice = inputValidator.getValidInt("Select an option: ", 1, 2);

            if (choice == 2) {
                playAgain = false;
                System.out.println("\nThanks for playing Splendor!");
            }
        }
    }
}
