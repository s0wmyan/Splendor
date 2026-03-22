package app;

import model.*;
import action.*;
import game.*;
import ui.*;
import engine.*;
import java.util.*;


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
        Map<String, String> playerDetails = cManager.promptForPlayerDetails(playerCount);

        List<Player> players = new ArrayList<>();
        for (Map.Entry<String, String> entry : playerDetails.entrySet()) {
            String playerName = entry.getKey();
            String playerBirthdayString = entry.getValue(); // parse birthday  (DD/MM/YYYY)
            int day = Integer.parseInt(playerBirthdayString.substring(0, 2));
            int month = Integer.parseInt(playerBirthdayString.substring(3, 5));
            int year = Integer.parseInt(playerBirthdayString.substring(6, 10));

            Birthday playerBirthday = new Birthday(day, month, year);
            // Initialize Player

            players.add(new HumanPlayer(playerName, playerBirthday, cManager));
        }


        // need a function to sort players

        Board board = new Board(buildDeck(1), buildDeck(2), buildDeck(3), buildNobles());
        seedBank(board.getBank(), playerCount);
        board.refillAll();
        TurnManager turnManager = new TurnManager(playerCount);
        GameState gameState = new GameState(board, players, turnManager);
        Game game = new Game(gameState);
        game.run();

        // console manager
        // game state
        // game
    
    }
}
