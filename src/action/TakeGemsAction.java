package action;

import java.util.*;
import game.*;
import model.*;

public class TakeGemsAction implements Action {
    private Map<GemType,Integer> selections;

    public TakeGemsAction(Map<GemType,Integer> selections) {
        this.selections = selections;
    }

    public String validate(GameState state) {
        Board board = state.getBoard();
        Bank bank = state.getBank();

        // player cannot choose gold tokens
        if (selections.containsKey(GemType.GOLD)) {
            return "You cannot choose gold!";
        }

        // if player chooses 2 of a type, bank must have at least 4 of that type
        if (selections.size() == 1 && selections.containsValue(2)) {
            for (GemType type : selections.keySet()) {
                int bankStock = bank.getTokenCount(type);
                if (bankStock < 4) {
                    return "The bank does not have sufficient " + type.name() + " tokens for you to take 2 of the same token!";
                }
            }
            return null; // works
        }

        // check if bank has the tokens player wants
        if (selections.size() == 3) {
            for (GemType type : selections.keySet()) {
                int count = selections.get(type);
                if (count > 1) {
                    return "You cannot take more than 1 token of the same color as you have taken 3 tokens!";
                }
                int bankStock = bank.getTokenCount(type);
                if (bankStock < 1) {
                    return "The bank does not have sufficient " + type.name() + " tokens";
                }
            }
            return null; // works 
        }

        // when it doesn't fufill any criteria = take too many or too little
        return "You can only take two tokens of the same color, or three tokens of different colors!";
    }

    public void apply(GameState state) {
        Bank bank = state.getBank();

        Player player = state.getCurrentPlayer();
        Map<GemType, Integer> wallet = player.getTokens();

        // add tokens to player's wallet
        for (GemType type : selections.keySet()) {
            int bankStock = bank.getTokenCount(type);
            int amountToTake = selections.get(type);
            bank.removeTokenCount(type, amountToTake);

            int amountOwned = wallet.get(type);
            wallet.put(type, amountOwned + amountToTake);
        }


    }
    public String getSuccessMessage(GameState state) {
        Player player = state.getCurrentPlayer();
        return "Player " + player.getName() + " has taken tokens: " + GemType.formatGemMap(selections);
    }
}
