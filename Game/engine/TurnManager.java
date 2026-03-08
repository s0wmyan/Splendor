package game.engine;

public class TurnManager{

    private InputValidater inputValidater;
    private ConsoleManager view;

    public void executeTurn(GameState gameState) {

        // get player
        Player player = gameState.getCurrentPlayer();

        // display current board layout
        view.displayBoard(gameState);

        // display hand
        view.displayHand(Player);

        // get hand
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
            case 1: 
                Map<Token, Integer> = inputValidater.
                return new TakeGemAction(tokens)
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

    public void checkNobleVisitCondition(Player player, Board board) {

    }

    public void refillBoard(Board board) {

    }
}