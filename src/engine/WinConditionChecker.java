package engine;

import java.util.*;
import model.*;

public class WinConditionChecker {

    private int winningPrestigeRequirement;

    // default constructor (just in case)
    public WinConditionChecker() {
        this.winningPrestigeRequirement = 15;
    }

    // supposed constructor to use
    public WinConditionChecker(int winningPrestigeRequirement) {
        this.winningPrestigeRequirement = winningPrestigeRequirement;
    }

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
    public Player getWinner(List<Player> players) {

        // put the players in a list based on whether they have more than 15 prestige points 
        List<Player> winningPlayerList = new ArrayList<>();

        for (Player player : players) {

            int points = player.getPrestigePoints();
            if (points >= winningPrestigeRequirement) {
                winningPlayerList.add(player);
            }    
        }

        // if there is no player in the winning player list, 
        // return null
        if (winningPlayerList.isEmpty()) {
            return null;
        }
        
        // if there is only 1 player in the list, 
        // return that player
        if (winningPlayerList.size() == 1) {
            return winningPlayerList.get(0);
        }

        Player bestWinner = winningPlayerList.get(0);

        // filtering mechanic to decide the winner based on the rules
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

        // return the winner of the game
        return bestWinner;
    }
}