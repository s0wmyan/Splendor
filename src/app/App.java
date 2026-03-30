package app;

import model.*;
import action.*;
import game.*;
import ui.*;
import engine.*;
import util.*;
import java.util.*;


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
            nobles.removeLast();
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
            // Initialize Player
            Player player = new HumanPlayer(playerName, playerBirthday);
            players.add(player);

            System.out.println("Player " + index++ + " - " + player.toString());
        }


        // need a function to sort players

        Board board = new Board(buildDeck(1, config), buildDeck(2, config), buildDeck(3, config), buildNobles(playerCount, config));
        seedBank(board.getBank(), playerCount, config);
        board.refillAll();

        // setup win condition
        int winningPrestigeRequirement = config.getIntProperty("winningConditionPoints", 15);
        WinConditionChecker winConditionChecker = new WinConditionChecker(winningPrestigeRequirement);

        int firstPlayerIndex = 1; // => to be defined 
        TurnManager turnManager = new TurnManager(inputValidator, cManager, winConditionChecker, playerCount, firstPlayerIndex); 

        GameState gameState = new GameState(board, players);
        Game game = new Game(gameState, turnManager);

        // prompt for action until someone wins 
        System.out.println("Game is Starting !");
        game.run(); // => needs to be done. 
    
    }
}
