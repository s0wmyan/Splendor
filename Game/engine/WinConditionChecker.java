package game.engine;

import java.util.*;
public class WinConditionChecker {

    private int winningPrestigeRequirement = 15;

    public boolean winnerList(ArrayList<Player> players) {
        for (Player player : players) {
            
            if (player.getPrestigePoints >= winningPrestigeRequirement) {
                return true;
            }
        }
        return false;
    }

    public Player getWinner(ArrayList<Player> players) {
        ArrayList<Player> winningPlayerList = new ArrayList<>();

        for (Player player : players) {

            int points = player.getPrestigePoints();
            if (points >= winningPrestigeRequirement) {
                winningPlayerList.add(points);
            }    
        }
        
        if (winningPlayerList.size() == 1) {

            return winningPlayerList.get(0);
        }

        // do the last part of the tiebreaker ltr
        
    }
}