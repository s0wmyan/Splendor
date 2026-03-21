package engine;

import java.util.*;
import model.*;

public class WinConditionChecker {

    private final int winningPrestigeRequirement = 15;

    // Get the list of current winners
    public boolean hasWinner(List<Player> players) {
        for (Player player : players) {
            
            if (player.getPrestigePoints() >= winningPrestigeRequirement) {
                return true;
            }
        }
        return false;
    }

    // From the list of winners, sort the players based on the winning conditions satisfied
    public Player getWinner(ArrayList<Player> players) {
        ArrayList<Player> winningPlayerList = new ArrayList<>();

        for (Player player : players) {

            int points = player.getPrestigePoints();
            if (points >= winningPrestigeRequirement) {
                winningPlayerList.add(player);
            }    
        }
        
        if (winningPlayerList.size() == 1) {

            return winningPlayerList.get(0);
        }

        // do the last part of the tiebreaker ltr
        Player bestWinner = winningPlayerList.get(0);
        for (Player player : winningPlayerList) {

            List<DevelopmentCard> playerCardsOwned = player.getPurchasedCards();
            List<DevelopmentCard> winnerCardsOwned = bestWinner.getPurchasedCards();
            if (player.getPrestigePoints() > bestWinner.getPrestigePoints()) {
                bestWinner = player;

            } else if (playerCardsOwned.size() < winnerCardsOwned.size() && 
                        player.getPrestigePoints() == bestWinner.getPrestigePoints())  {
                bestWinner = player;
            }
        }

        return bestWinner;
    }
}