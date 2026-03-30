package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import model.Noble;
import model.Player;
import ui.ConsoleManager;
import ui.InputValidator;


public class App {

    private static void seedBank(Bank bank, int playerCount) {
        int standard = switch (playerCount) {
            case 2 -> 4;
            case 3 -> 5;
            default -> 7;
        };
        for (GemType type : GemType.getStandardGems()) {
            bank.setTokenCount(type, standard);
        }
        bank.setTokenCount(GemType.GOLD, 5);
    }

    private static Deck buildDeck(int level) {
        List<DevelopmentCard> cards = new ArrayList<>();
        // temporary example for the cards 
        if (level == 1) {
            cards.add(card(1, GemType.RED, 0, GemType.BLUE, 1, GemType.GREEN, 1));
            cards.add(card(1, GemType.BLUE, 0, GemType.WHITE, 2));
            cards.add(card(1, GemType.WHITE, 0, GemType.BLACK, 1, GemType.RED, 1));
            cards.add(card(1, GemType.GREEN, 0, GemType.RED, 2));
            cards.add(card(1, GemType.BLACK, 1, GemType.WHITE, 2, GemType.BLUE, 1));
        } else if (level == 2) {
            cards.add(card(2, GemType.RED, 1, GemType.BLUE, 3, GemType.GREEN, 2));
            cards.add(card(2, GemType.BLUE, 2, GemType.WHITE, 3, GemType.BLACK, 2));
            cards.add(card(2, GemType.WHITE, 1, GemType.RED, 2, GemType.GREEN, 2));
            cards.add(card(2, GemType.GREEN, 2, GemType.BLACK, 3, GemType.BLUE, 2));
            cards.add(card(2, GemType.BLACK, 1, GemType.WHITE, 2, GemType.RED, 2));
        } else {
            cards.add(card(3, GemType.RED, 3, GemType.BLUE, 3, GemType.GREEN, 3, GemType.WHITE, 3));
            cards.add(card(3, GemType.BLUE, 4, GemType.BLACK, 3, GemType.WHITE, 3));
            cards.add(card(3, GemType.WHITE, 3, GemType.RED, 3, GemType.GREEN, 3));
            cards.add(card(3, GemType.GREEN, 4, GemType.BLUE, 3, GemType.BLACK, 3));
            cards.add(card(3, GemType.BLACK, 3, GemType.RED, 3, GemType.WHITE, 3));
        }
        Deck deck = new Deck(level, cards);
        deck.shuffle();
        return deck;
    }

    // helper method to create card objects 
    private static DevelopmentCard card(int level, GemType bonus, int points, Object... costs) {
        Map<GemType, Integer> cost = new EnumMap<>(GemType.class);
        for (int i = 0; i < costs.length; i += 2) {
            cost.put((GemType) costs[i], (Integer) costs[i + 1]);
        }
        return new DevelopmentCard(level, bonus, cost, points);
    }

    private static List<Noble> buildNobles() {
        return List.of(
            noble("Noble A", GemType.RED, 3, GemType.BLUE, 3, GemType.WHITE, 3),
            noble("Noble B", GemType.GREEN, 3, GemType.BLACK, 3, GemType.RED, 3),
            noble("Noble C", GemType.WHITE, 4, GemType.BLUE, 4),
            noble("Noble D", GemType.GREEN, 4, GemType.BLACK, 4)
        );
    }

    // helper method to create noble objects 
    private static Noble noble(String name, Object... costs) {
        Map<GemType, Integer> cost = new EnumMap<>(GemType.class);
        for (int i = 0; i < costs.length; i += 2) {
            cost.put((GemType) costs[i], (Integer) costs[i + 1]);
        }
        return new Noble(name, cost, 3);
    }

    public static void main(String[] args) {
        
        // get input for the number of players etc
        Scanner sc = new Scanner(System.in);
        InputValidator inputValidator = new InputValidator(sc);
        ConsoleManager cManager = new ConsoleManager(inputValidator);

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
            players.sort(
            Comparator.comparing(Player::getBirthday)
                .reversed()                              // youngest (latest date) first
                .thenComparing(Player::getName));        

            System.out.println("Player " + index++ + " - " + player.toString());
        }


        // need a function to sort players

        Board board = new Board(buildDeck(1), buildDeck(2), buildDeck(3), buildNobles());
        seedBank(board.getBank(), playerCount);
        board.refillAll();

        WinConditionChecker winConditionChecker = new WinConditionChecker();
        int firstPlayerIndex = 0;
        TurnManager turnManager = new TurnManager(inputValidator, cManager, winConditionChecker, playerCount, firstPlayerIndex); 

        GameState gameState = new GameState(board, players);
        Game game = new Game(gameState, turnManager);

        gameState.setPlayerIndex(firstPlayerIndex);
        // prompt for action until someone wins 
        System.out.println("Game is Starting !");
        game.run(); // => needs to be done. 
    
    }
}
