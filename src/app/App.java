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

    // // helper method to create card objects 
    // private static DevelopmentCard card(int level, GemType bonus, int points, Object... costs) {
    //     Map<GemType, Integer> cost = new EnumMap<>(GemType.class);
    //     for (int i = 0; i < costs.length; i += 2) {
    //         cost.put((GemType) costs[i], (Integer) costs[i + 1]);
    //     }
    //     return new DevelopmentCard(level, bonus, cost, points);
    // }

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

    // // helper method to create noble objects 
    // private static Noble noble(String name, Object... costs) {
    //     Map<GemType, Integer> cost = new EnumMap<>(GemType.class);
    //     for (int i = 0; i < costs.length; i += 2) {
    //         cost.put((GemType) costs[i], (Integer) costs[i + 1]);
    //     }
    //     return new Noble(name, cost, 3);
    // }

    public static void main(String[] args) {
        // get input for the number of players etc
        Scanner sc = new Scanner(System.in);
        InputValidator inputValidator = new InputValidator(sc);
        ConsoleManager cManager = new ConsoleManager(inputValidator);
        ConfigLoader config = new ConfigLoader("config.properties");

        // check gamemode and no of players
        int gameMode = cManager.promptForGameMode();
        int playerCount = cManager.promptForPlayerCount();

        System.out.println("No. of players: " + playerCount);

        List<Player> players = new ArrayList<>();
        int firstPlayerIndex = 0;

        // mode 1: human vs humans
        if (gameMode == 1) {
            Map<String, String> playerDetails = cManager.promptForPlayerDetails(playerCount);

            // initialize the humans
            for (Map.Entry<String, String> entry : playerDetails.entrySet()) {
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
            System.out.println("\nEnter your details ");
            Map<String, String> humanDetails = cManager.promptForPlayerDetails(1);
            Map.Entry<String, String> entry = humanDetails.entrySet().iterator().next();

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

        // old version
        /*
        // Initialize game state
            // initialize players -> initialize board -> initialize game state 

        int playerCount = cManager.promptForPlayerCount();

        System.out.println("No. of Players: " + playerCount);
        Map<String, String> playerDetails = cManager.promptForPlayerDetails(playerCount);

        List<Player> players = new ArrayList<>();
        int index = 1;

        for (Map.Entry<String, String> entry : playerDetails.entrySet()) {
            String playerName = entry.getKey();
            String playerBirthdayString = entry.getValue(); // parse birthday  (DD/MM/YYYY)
            int day = Integer.parseInt(playerBirthdayString.substring(0, 2));
            int month = Integer.parseInt(playerBirthdayString.substring(3, 5));
            int year = Integer.parseInt(playerBirthdayString.substring(6, 10));

            Birthday playerBirthday = new Birthday(day, month, year);

            // ask what type of Player it is: computer or human
            boolean isComputer = cManager.promptForComputerPlayer(playerName);
            Player player;

            // initialize player
            if (isComputer) {
                player = new ComputerPlayer(playerName, playerBirthday);
            } else {
                player = new HumanPlayer(playerName, playerBirthday);
            }

            // add the player and print
            players.add(player);

            // sort the list of players, youngest first
            players.sort(
                Comparator.comparing(Player::getBirthday)
                .reversed()                              // youngest (latest date) first
                .thenComparing(Player::getName));        

            // print turn order
            System.out.println("\nOffical turn order");
            for (int i = 0; i < players.size(); i++) {
                System.out.println("Player " + (i + 1) + " - " + players.get(i).toString());
            }
        }
        */

        Board board = new Board(buildDeck(1, config), buildDeck(2, config), buildDeck(3, config), buildNobles(playerCount, config));
        seedBank(board.getBank(), playerCount, config);
        board.refillAll();

        // setup win condition
        int winningPrestigeRequirement = config.getIntProperty("winningConditionPoints", 15);
        WinConditionChecker winConditionChecker = new WinConditionChecker(winningPrestigeRequirement);

        TurnManager turnManager = new TurnManager(inputValidator, cManager, winConditionChecker, playerCount, firstPlayerIndex); 

        GameState gameState = new GameState(board, players);
        Game game = new Game(gameState, turnManager);

        gameState.setPlayerIndex(firstPlayerIndex);

        // prompt for action until someone wins 
        System.out.println("Game is Starting !");
        game.run(); // => needs to be done. 
    }
}
