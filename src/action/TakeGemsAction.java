package action;

import java.util.Map;

import game.GameState;
import model.Bank;
import model.Board;
import model.GemType;
import model.Player;

public class TakeGemsAction implements Action {
    private Map<GemType,Integer> selections;

    public TakeGemsAction(Map<GemType,Integer> selections) {
        this.selections = selections;
    }

    public boolean validate(GameState state) {
        Bank bank = state.getBank();

        if (selections.getOrDefault(GemType.GOLD, 0) > 0) {
            return false;
        }

        if (selections.size() == 1 && selections.containsValue(2)) {
            for (GemType type : selections.keySet()) {
                int bankStock = bank.getTokenCount(type);
                if (bankStock < 4) {
                    return false;
                }
            }
            return true;
        }

        int total = 0;
        int distinct = selections.size();

        for (int count : selections.values()) {
            total += count;
        }

        // Case 1: 3 different gems
        if (total == 3 && distinct == 3) {
            for (int count : selections.values()) {
                if (count != 1) return false;
            }
            return true;
        }

        // Case 2: 2 same gems
        if (total == 2 && distinct == 1) {
            for (int count : selections.values()) {
                if (count == 2) return true;
            }
        }

        return false;

        // if (selections.size() == 3) {
        //     for (GemType type : selections.keySet()) {
        //         int count = selections.get(type);
        //         if (count != 1) {
        //             return false;
        //         }
        //         int bankStock = bank.getTokenCount(type);
        //         if (bankStock < 1) { 
        //             return false;
        //         }
        //     }
        //     return true;
        // }

        // return false;
    }

    public void apply(GameState state) {
        Bank bank = state.getBank();

        Player player = state.getCurrentPlayer();
        Map<GemType, Integer> wallet = player.getTokens();

        for (GemType type : selections.keySet()) {
            int bankStock = bank.getTokenCount(type);
            int amountToTake = selections.get(type);
            bank.removeTokenCount(type, amountToTake);

            int amountOwned = wallet.get(type);
            wallet.put(type, amountOwned + amountToTake);
        }

    }

    public void returnTokenToBank(GameState state, GemType toReturn) {
        Player player = state.getCurrentPlayer();
        int playerStock = player.getTokens().get(toReturn);
        player.getTokens().put(toReturn, playerStock - 1);

        Bank bank = state.getBank();
        int bankStock = bank.get(toReturn);
        bank.put(toReturn, bankStock + 1);
    }
}
