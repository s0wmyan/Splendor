package action;

import java.util.*;
import game.*;
import model.*;

public class TakeGemsAction implements Action {
    private Map<GemType,Integer> selections;

    public TakeGemsAction(Map<GemType,Integer> selections) {
        this.selections = selections;
    }

    public boolean validate(GameState state) {
        Board board = state.getBoard();
        Bank bank = state.getBank();

        if (selections.containsKey(GemType.GOLD)) {
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

        if (selections.size() == 3) {
            for (GemType type : selections.keySet()) {
                int count = selections.get(type);
                if (count != 1) {
                    return false;
                }
                int bankStock = bank.getTokenCount(type);
                if (bankStock < 1) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public void apply(GameState state, Board board) {
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
}
