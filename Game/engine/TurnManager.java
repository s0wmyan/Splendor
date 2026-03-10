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
        gameState.checkNobleVisitCondition(player, board);
        
        // Now that the player has taken action, you need to fill up the board (provided the player has a taken a card from the board)
        gameState.refillBoard(board);

        // get the next player 
        gameState.nextTurn();
    }

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
                Card buyingCard = inputValidater.selectCard(gameState.getBoard().canBuyCards(player));
                return new BuyCardAction(buyingCard);
            // ReserveCardAction
            case 3:
                Card reservingCard = inputValidater.selectCard(gameState.getBoard().getAllFaceUpCards());
                return new ReserveCardAction(reservingCard);
            // user keys in the wrong input
            default:
                System.out.println("Invalid number/input! Please try again.");
                return promptAction(Player);
        }
    }

    // this method will be placed in board
    // private ArrayList<Card> allFaceUpCards(Player player) {

    //     ArrayList<Card> tier1FaceUpCards = gameState.getBoard().getLevel1FaceUp();
    //     ArrayList<Card> tier2FaceUpCards = gameState.getBoard().getLevel2FaceUp();
    //     ArrayList<Card> tier3FaceUpCards = gameState.getBoard().getLevel3FaceUp();

    //     ArrayList<Card> allFaceUpCards = new ArrayList<>();

    //     allFaceUpCards.addAll(tier1FaceUpCards);
    //     allFaceUpCards.addAll(tier2FaceUpCards);
    //     allFaceUpCards.addAll(tier3FaceUpCards);

    //     return allFaceUpCards;
    // }

    private ArrayList<Card> canBuyCards(Player player) {
        List <Card> canBuyList = new ArrayList<>();

        ArrayList<Card> allFaceUpCards = gameState.getBoard().getAllFaceUpCards();

        // Loop through all the cards that are faceup right now on the board
        for (Card card: allFaceUpCards) {
            if (canBuy(player, card)) {
                canBuyList.add(card);
            }
        }

        // Loop through the reservedCards that the player currently has 
        for (Card card: player.getReservedCards()) {
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
            int bonus = bonuses.getOrDefault(Gemtype, 0);

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
        List<Noble> eligibleList = getEligibleNobles(player, board.getNobles());

        if (eligible.size() == 1) {

            Noble noble = eligibleList.get(0);
            player.addNoble(noble); // Need to add this into the UML diagram
            board.removeNoble(noble);
        } else if (eligible.size() > 1) {

            // since the player has satisfied the conditions of multiple nobles,
            // he needs to select one of the nobles that he satisfied to add to his own
            Noble noble = inputValidater.selectNoble(eligibleList);
            player.addNoble(noble);
            board.removeNoble(noble);
        }

    }

    private ArrayList<Noble> getEligibleNobles(Player player, List<Noble> nobles) {
        List<Noble> eligible = new ArrayList<>();

        Map<GemType, Integer> bonuses = player.getBonuses();

        for (Noble noble : nobles) {

            boolean canVisit = true;

            Map<GemType, Integer> requirements = noble.getRequirements();
            
            for (Map.Entry<GemType, Integer> entry : requirements.entrySet()) {

                Gemtype gemType = entry.getKey();
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

    public void refillBoard(Board board) {
        List<Card> level1FaceUp = board.getLevel1FaceUp();
        List<Card> level2FaceUp = board.getLevel2FaceUp();
        List<Card> level3FaceUp = board.getLevel3FaceUp();
        
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
}