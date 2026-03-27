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
    private int currentPlayerIndex;

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
        this.currentPlayerIndex = firstPlayerIndex;
    }

    public void executeTurn(GameState gameState) {

        this.gameState = gameState;

        // runs a loop that continues
        // this needs to be under game
        // and needs to have 
        // private GameState gameState;
        // while (!winConditionChecker.hasWinner(gameState.getPlayers())) {
        //     executeTurn(gameState);
        // }

        // get player
        // Player player = gameState.getPlayers().get(currentPlayerIndex); // replace
        Player player = gameState.getCurrentPlayer();


        // display current board layout
        view.displayGame(gameState);

        // display hand
        // view.displayHand(player);

        // display player
        view.displayPlayerStatus(player); // includes hand too

        // get action from player
        Action action = promptAction(player); 

        // get board
        Board board = gameState.getBoard();

        // Check if the action is legal
        while (!action.validate(gameState)) {
            System.out.println("Invalid action! Try another action.\n"); // why exactly is it invalid? can throw an error and catch it 
            action = promptAction(player); // continue prompting until it is valid 
        }    

        // if the action is valid, apply the action to the gameState
        action.apply(gameState);

        // call the checkNobleVisitCondition method to see if got lucky noble visit
        checkNobleVisitCondition(player, board);
        
        // Now that the player has taken action, you need to fill up the board (provided the player has a taken a card from the board)
        refillBoard(board);

        // get the next player 
        advanceToNextPlayer();
    }

    // settled
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
        System.out.println("");
        System.out.print("Key in an action: ");

        // inputValidater calls on getValidInt
        int choice = inputValidator.getValidInt();

        switch (choice) {
            // TakeGemAction
            case 1: 
                // making changes on their end
                // getBank() returns bank, not a map
                Map<GemType, Integer> GemTypes = view.selectGems(gameState.getBoard().getBank().getTokens());
                return new TakeGemsAction(GemTypes);
            // BuyCardAction takes in a card and from reserved
            case 2: 
                // ignore error for now
                // because board has not been changed to 
                // get the list of buyable cards that the player can buy now
                List<DevelopmentCard> buyableCards = canBuyCards(player); // => issue 
                System.out.println(buyableCards); // no cards are buyable 

                // player now selects the card
                DevelopmentCard buyingCard = view.selectCard(player, buyableCards);

                // does the player select a currently reserved card?
                boolean isReserved = player.getReservedCards().contains(buyingCard);

                // There might be something wrong with buyCardAction
                return new BuyCardAction(buyingCard, isReserved);
            // ReserveCardAction
            case 3:
                // ignore error for now
                // because board has not been changed to DevelopmentCard
                DevelopmentCard reservingCard = view.selectCard(player, gameState.getBoard().allFaceUpCards());
                return new ReserveCardAction(reservingCard);
            // user keys in the wrong input
            default:
                System.out.println("Invalid number/input! Please try again.");
                return promptAction(player);
        }
    }

    // Produces an List<Card> to show what cards you can afford
    public List<DevelopmentCard> canBuyCards(Player player) {
        List<DevelopmentCard> canBuyList = new ArrayList<>();

        // board has not been changed to developmentcard
        // ignore this error
        List<DevelopmentCard> allFaceUpCards = gameState.getBoard().allFaceUpCards();
        // System.out.println(allFaceUpCards); // not empty

        // Loop through all the cards that are faceup right now on the board
        for (DevelopmentCard card: allFaceUpCards) {
            if (canBuy(player, card)) {
                canBuyList.add(card);
            }
        }

        // Loop through the reservedCards that the player currently has 
        for (DevelopmentCard card: player.getReservedCards()) {
            if (canBuy(player, card)) {
                canBuyList.add(card);
            }
        }
        // board has not been changed to developmentcard; ignore this error
        return canBuyList;
    }

    // Checks whether you are eligible to buy the card
    private boolean canBuy(Player player, DevelopmentCard card) {
        Map<GemType, Integer> trueCost = getTrueCost(player, card);
        Map<GemType, Integer> gems = player.getTokens();

        for (Map.Entry<GemType, Integer> entry : trueCost.entrySet()) {
            int required = entry.getValue();

            // Look up the enum of the players gems. 
            // If the player doesn't have any gems, then it will be defaulted to 0
            int currentlyOwned = gems.getOrDefault(entry.getKey(), 0); 

            int moreRequired = required - currentlyOwned;
            if (moreRequired > 0) {

                if (gems.getOrDefault(GemType.GOLD, 0) < moreRequired) {
                    return false;
                }
            }
        }

        return true;
    }

    // Returns a map that maps the gems to the real cost, of a single card
    public Map<GemType, Integer> getTrueCost(Player player, DevelopmentCard card) { // change name to something else
        Map<GemType, Integer> trueCost = new HashMap<>();
        Map<GemType, Integer> bonuses = player.getBonuses();

        for (Map.Entry<GemType, Integer> entry : card.getCost().entrySet()) { 
            GemType gemType = entry.getKey();
            int cost = entry.getValue();
            int bonus = bonuses.getOrDefault(gemType, 0);

            // The trueCost cannot go below 0
            int remaining = cost - bonus > 0 ? cost - bonus : 0;

            if (remaining > 0) {
                trueCost.put(gemType, remaining);
            }
        }

        return trueCost;
    }

    // Has a list of eligibleNobles that the player can get
    // Adds nobles to their stash and removes from the board
    public void checkNobleVisitCondition(Player player, Board board) {
        List<Noble> eligibleList = getEligibleNobles(player, board.getNobles());

        if (eligibleList.size() == 1) {

            Noble noble = eligibleList.get(0);
            player.addNoble(noble); // Need to add this into the UML diagram
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

    // Refill the board once a card(s) has been taken off the board
    public void refillBoard(Board board) {
        List<DevelopmentCard> level1FaceUp = board.getLevel1FaceUp();
        List<DevelopmentCard> level2FaceUp = board.getLevel2FaceUp();
        List<DevelopmentCard> level3FaceUp = board.getLevel3FaceUp();
        
        while (level1FaceUp.size() < 4 && !board.getDeck(1).isEmpty()) {
            board.refillLevel(1);
        }

        while (level2FaceUp.size() < 4 && !board.getDeck(2).isEmpty()) {
            board.refillLevel(2);
        }

        while (level3FaceUp.size() < 4 && !board.getDeck(3).isEmpty()) {
            board.refillLevel(3);
        }
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getFirstPlayerIndex() {
        return firstPlayerIndex;
    }

    public int getCurrentPlayerIndex() {
        return gameState.getCurrentPlayerIndex();
        // return currentPlayerIndex;
    }

    public void advanceToNextPlayer() {
        // currentPlayerIndex = (currentPlayerIndex + 1) % playerCount;
        gameState.setPlayerIndex((gameState.getCurrentPlayerIndex() + 1) % playerCount);
    }

    public boolean hasWinner(List<Player> playerList) {
        return winConditionChecker.hasWinner(playerList);
    }
}

// public TurnManager(int playerCount) {
    //     this(playerCount, 0);
    // }

    // // Look at these two constructors another day

    // public TurnManager(int playerCount, int firstPlayerIndex) {
    //     if (playerCount < 2) {
    //         throw new IllegalArgumentException("At least 2 players required");
    //     }
    //     this.playerCount = playerCount;
    //     this.firstPlayerIndex = firstPlayerIndex;
    //     this.currentPlayerIndex = firstPlayerIndex;
    // }

    // ComputerPlayer will choose an action that they can make
    // public Action promptBotAction(ComputerPlayer computerPlayer) {

    //     // prompts the user for an action input
    //     System.out.println("Your turn, " + player.getName());
    //     System.out.println("(1): Take tokens");
    //     System.out.println("(2): Buy a card");
    //     System.out.println("(3): Reserve a card");
    //     System.out.println("");
    //     System.out.print("Key in an action: ");

    //     // inputValidater calls on getValidInt
    //     // asking to change this
    //     int choice = inputValidator.getValidInt(); // has not yet been implemented

    //     switch (choice) {
    //         // TakeGemAction
    //         case 1: 
    //             Map<GemType, Integer> GemTypes = inputValidator.selectGems(gameState.getBoard().getBank());
    //             return new TakeGemsAction(GemTypes);
    //         // BuyCardAction takes in a card and from reserved
    //         case 2: 
    //             DevelopmentCard buyingCard = inputValidator.selectCard(canBuyCards(player));

    //             // There might be something wrong with buycardaction
    //             return new BuyCardAction(buyingCard);
    //         // ReserveCardAction
    //         case 3:
    //             DevelopmentCard reservingCard = inputValidator.selectCard(gameState.getBoard().getAllFaceUpCards());
    //             return new ReserveCardAction(reservingCard);
    //         // user keys in the wrong input
    //         default:
    //             System.out.println("Invalid number/input! Please try again.");
    //             return promptAction(player);
    //     }
    // }