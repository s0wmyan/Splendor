package game.engine;

import java.util.*;
public class TurnManager{

    private InputValidater inputValidater;
    private ConsoleManager view;
    private GameState gameState;

    public void executeTurn(GameState gameState) {

        this.gameState = gameState;
        // get player
        Player player = gameState.getCurrentPlayer();

        // display current board layout
        view.displayBoard(gameState);

        // display hand
        view.displayHand(Player);

        // get action from player
        Action action = promptAction(Player);

        // Check if the action is legal
        if (!action.validate()) {
            System.out.println("Invalid action!");
        }    

        // if the action is valid, apply the action to the gameState
        action.apply();

        // get board
        Board board = gameState.getBoard();

        // call the checkNobleVisitCondition method to see if got lucky noble visit
        gameState.checkNobleVisitCondition();
        
        // Now that the player has taken action, you need to fill up the board (provided the player has a taken a card from the board)
        gameState.refillBoard();

        // get the next player 
        gameState.nextTurn();
    }

    // Incomplete
    public Action promptAction(Player player) {

        // prompts the user for an action input
        System.out.println("Your turn, " + player.getName());
        System.out.println("(1): Take tokens");
        System.out.println("(2): Buy a card");
        System.out.println("(3): Reserve a card");
        System.out.println("");
        System.out.print("Key in an action: ");

        // inputValidater calls on getValidInt
        int choice = inputValidater.getValidInt();

        switch (choice) {
            // TakeGemAction
            case 1: 
                Map<GemType, Integer> = inputValidater.selectGems(gameState.getBoard().getBank());
                return new TakeGemAction(GemsType);
            // BuyCardAction takes in a card and from reserved
            case 2: 
                Card card = inputValidater.selectCard(gameState.getBoard().canBuyCards(player));
                return new BuyCardAction(card);
            // ReserveCardAction
            case 3:
            default:
                System.out.println("Invalid number/input! Please try again.");
                return promptAction(Player);
        }
    }

    public Action promptAction(Player player) {
        // 1. Show the player their options
        System.out.println("\nYour turn, " + player.getName());
        System.out.println("1) Take Tokens");
        System.out.println("2) Purchase Card");
        System.out.println("3) Reserve Card");
        System.out.print("Choose an action: ");

        // 2. Get their choice
        int choice = inputHandler.getChoice();

        // 3. Based on choice, gather more info and build the Action
        switch (choice) {
            case 1:
                Map<Token, Integer> tokens = inputHandler.selectTokens(state.getBoard().getBank());
                return new TakeTokensAction(tokens);

            case 2:
                Card cardToBuy = inputHandler.selectCard(getAffordableCards(player));
                return new PurchaseCardAction(cardToBuy);

            case 3:
                Card cardToReserve = inputHandler.selectCard(state.getBoard().getAllFaceUpCards());
                return new ReserveCardAction(cardToReserve);

            default:
                System.out.println("Invalid choice, try again");
                return promptAction(player); // recurse and ask again
        }
    }

    private ArrayList<Card> canBuyCards(Player player) {
        List <Card> canBuyList = new ArrayList<>();

        ArrayList<Card> tier1FaceUpCards = gameState.getBoard().getLevel1FaceUp();
        ArrayList<Card> tier2FaceUpCards = gameState.getBoard().getLevel2FaceUp();
        ArrayList<Card> tier3FaceUpCards = gameState.getBoard().getLevel3FaceUp();

        ArrayList<Card> allFaceUpCards = new ArrayList<>();

        allFaceUpCards.addall(tier1FaceUpCards);
        allFaceUpCards.addall(tier2FaceUpCards);
        allFaceUpCards.addall(tier3FaceUpCards);

        for (Card card: allFaceUpCards) {
            if (canBuy(player, card)) {
                canBuyList.add(card);
            }
        }

        for (Card card: player.getReserved()) {
            if (canBuy(player, card)) {
                canBuyList.add(card);
            }
        }
        return canBuyList;
    }

    private boolean canBuy(Player player, Card card) {
        Map<GemType, Integer> trueCost = getTrueCost(player, card);
        Map<GemType, Integer> gems = player.getTokens();

        for (Map.Entry<GemType, Integer> entry : trueCost.entrySet()) {
            int required = entry.getValue();

            // Look up the enum of the players gems. 
            // If the player doesn't have any gems, then it will be defaulted to 0
            int currentlyOwned = gems.getOrDefault(entry.getKey(), 0); 

            int moreRequired = currentlyOwned - required;
            if (moreRequired < 0) {

                if (gems.getOrDefault(GemType.GOLD, 0) < moreRequired) {
                    return false;
                }
            }
        }

        return true;
    }

    public Map<GemType, Integer> getTrueCost(Player player, Card card) { // change name to something else
        Map<GemType, Integer> trueCost = new HashMap<>();
        Map<GemType, Integer> bonuses = player.getBonuses();

        for (Map.Entry<GemType, Integer> entry : card.getCost().entrySet()) { 
            GemType gemType = entry.getKey();
            int cost = entry.getValue();
            int bonus = bonues.getOrDefault(Gemtype, 0);

            // The trueCost cannot go below 0
            int remaining = cost - bonus > 0 ? cost - bonus : 0;

            if (remaining > 0) {
                trueCost.put(gemType, remaining);
            }
        }

        return trueCost;
    }

    // Incomplete
    public void checkNobleVisitCondition(Player player, Board board) {

    }

    // Incomplete
    public void refillBoard(Board board) {

    }
}