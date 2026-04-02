package engine;

import action.*;
import game.GameState;
import java.util.*;
import model.Board;
import model.ComputerPlayer;
import model.DevelopmentCard;
import model.GemType;
import model.Noble;
import model.Player;
import ui.*;



public class TurnManager{

    private InputValidator inputValidator;
    private ConsoleManager view;
    private GameState gameState;
    private WinConditionChecker winConditionChecker;

    private final int playerCount;
    private final int firstPlayerIndex;

    // missing a constructor 
    public TurnManager(InputValidator inputValidator, ConsoleManager view, 
                   WinConditionChecker winConditionChecker,
                   int playerCount, int firstPlayerIndex) {
        if (playerCount < 2) {
            throw new IllegalArgumentException("At least 2 players required");
        }
        this.inputValidator = inputValidator;
        this.view = view;
        this.winConditionChecker = winConditionChecker;
        this.playerCount = playerCount;
        this.firstPlayerIndex = firstPlayerIndex;
    }

    public void executeTurn(GameState gameState) {
        this.gameState = gameState;
        Player player = gameState.getCurrentPlayer();


        // display current board layout
        view.displayGame(gameState);

        // get action from player
        Action action = promptAction(player); 

        // get board
        Board board = gameState.getBoard();

        // Check if the action is legal
        String message = action.validate(gameState);
        while (message != null) {
            view.displayError(message);
            action = promptAction(player); // continue prompting until it is valid 
            message = action.validate(gameState); // try again
        }    

        // if the action is valid, apply the action to the gameState
        action.apply(gameState);

        // log it for analytics
        player.incrementTurnsTaken();

        // call the checkNobleVisitCondition method to see if got lucky noble visit
        checkNobleVisitCondition(player, board);
        
        // ensure player cannot have >10 tokens 
        enforceTokenLimit(player, board);

        // return message result
        view.displayMessage(action.getSuccessMessage(gameState));

        // Now that the player has taken action, you need to fill up the board. If no card taken, no cards will be refilled based on the method
        board.refillAll();

        // get the next player 
        advanceToNextPlayer();
    }

    public Action promptAction(Player player) {

        // If the player is a bot, prompt the bot for their input
        if (player instanceof ComputerPlayer) {
            return player.takeTurn(gameState);
        }

        // else, prompt the actual player for an input
        return promptHumanAction(player);
    }

    public Action promptHumanAction(Player player) {

        // prompts the user for an action input
        System.out.println("Your turn, " + player.getName());
        System.out.println("(1): Take tokens");
        System.out.println("(2): Buy a card");
        System.out.println("(3): Reserve a card");
        System.out.println("(4): Help / Rules");
        System.out.println("");

        int choice = inputValidator.getValidInt("Select an action (1-4): ", 1, 4);

        switch (choice) {
            // TakeGemAction
            case 1: 
                Map<GemType, Integer> gemTypes = view.selectGems(gameState.getBoard().getBank().getTokens());
                if (gemTypes == null) return promptAction(player);

                return new TakeGemsAction(gemTypes);
            // BuyCardAction takes in a card and from reserved
            case 2: 
                List<DevelopmentCard> boardCards = canBuyFromBoard(player); 
                List<DevelopmentCard> reservedCards = canBuyFromReserved(player); 
                
                if (boardCards.isEmpty() && reservedCards.isEmpty()) {
                    System.out.println("You don't have enough gems to buy a card");
                    return promptAction(player);
                }

                // player now selects the card
                DevelopmentCard buyingCard = view.selectCard(player, boardCards, reservedCards);
                if (buyingCard == null) return promptAction(player);
                
                // // does the player select a currently reserved card?
                boolean isReserved = player.getReservedCards().contains(buyingCard);

                // return the action for BuyCardAction
                return new BuyCardAction(buyingCard, isReserved);
            // ReserveCardAction
            case 3:
                DevelopmentCard reservingCard = view.selectCard(player, gameState.getBoard().allFaceUpCards());
                if (reservingCard == null) return promptAction(player);
                return new ReserveCardAction(reservingCard);
            case 4:
                view.displayHelp();
                return promptAction(player);
            
            // user keys in the wrong input 
            default:    
                System.out.println("Invalid number/input! Please try again.");
                return promptAction(player);
        }
    }

    // Produces an List<DevelopmentCard> to show what cards you can afford
    private List<DevelopmentCard> canBuyFromBoard(Player player) {
        List<DevelopmentCard> canBuyList = new ArrayList<>();
        List<DevelopmentCard> allFaceUpCards = gameState.getBoard().allFaceUpCards();

        // Loop through all the cards that are faceup right now on the board
        for (DevelopmentCard card: allFaceUpCards) {
            if (player.canAfford(card.getCost())) {
                canBuyList.add(card);
            }
        }
        return canBuyList;
    }

    // Returns a List<DevelopmentCard> to show what cards can be bought from your reservedCards
    private List<DevelopmentCard> canBuyFromReserved(Player player) {
        List<DevelopmentCard> canBuyList = new ArrayList<>();
        List<DevelopmentCard> reservedCards = player.getReservedCards();

        // Loop through the reservedCards that the player currently has 
        for (DevelopmentCard card: reservedCards) {
            if (player.canAfford(card.getCost())) {
                canBuyList.add(card);
            }
        }
        return canBuyList;
    }

    // Has a list of eligibleNobles that the player can get
    // Adds nobles to their stash and removes from the board
    private void checkNobleVisitCondition(Player player, Board board) {
        List<Noble> eligibleList = getEligibleNobles(player, board.getNobles());

        if (eligibleList.size() == 1) {
            Noble noble = eligibleList.get(0);
            player.addNoble(noble); 
            board.removeNoble(noble);
        } else if (eligibleList.size() > 1) {
            // since the player has satisfied the conditions of multiple nobles,
            // he needs to select one of the nobles that he satisfied to add to his own
            Noble noble = view.selectNoble(eligibleList);
            player.addNoble(noble);
            board.removeNoble(noble);
        }

    }

    // Get the ArrayList<Noble> from the board that the player is able to attract from the board
    private List<Noble> getEligibleNobles(Player player, List<Noble> nobles) {
        List<Noble> eligible = new ArrayList<>();

        Map<GemType, Integer> bonuses = player.getBonuses();

        for (Noble noble : nobles) {
            boolean canVisit = true;
            Map<GemType, Integer> requirements = noble.getCost();
            
            for (Map.Entry<GemType, Integer> entry : requirements.entrySet()) {
                GemType gemType = entry.getKey();
                int requiredValue = entry.getValue();
                int currentValue = bonuses.getOrDefault(gemType, 0);
                
                if (currentValue < requiredValue) {
                    canVisit = false;
                    break;
                }
            }

            if (canVisit) {
                eligible.add(noble);
            }
        }

        return eligible;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public WinConditionChecker getWinConditionChecker() {
        return winConditionChecker;
    }

    private void advanceToNextPlayer() {
        gameState.setPlayerIndex((gameState.getCurrentPlayerIndex() + 1) % playerCount);
    }
   
   private void enforceTokenLimit(Player player, Board board) {
        while (player.getTotalTokenCount() > 10) {
            int over = player.getTotalTokenCount() - 10;
            System.out.println(player.getName() + " has more than 10 tokens. Return " + over + " token(s).");

            List<GemType> options = new ArrayList<>();
            for (GemType type : GemType.values()) {
                if (player.getTokens().getOrDefault(type, 0) > 0) {
                    options.add(type);
                }
            }

            GemType chosen;

            if (player instanceof ComputerPlayer) {
                // bot picks a random one to discard
                chosen = options.get(new Random().nextInt(options.size()));
                System.out.println(player.getName() + " automatically returned a " + chosen.name() + " token.\n");
            } else {
                // Human picks
                System.out.println("Tokens: " + GemType.formatGemMap(player.getTokens()));
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i).name());
                }

                int pick = inputValidator.getValidInt("Select token to return: ", 1, options.size());
                chosen = options.get(pick - 1);
            }

            // Apply discard
            player.getTokens().put(chosen, player.getTokens().get(chosen) - 1);
            board.getBank().addTokenCount(chosen, 1);
        }
    }
}