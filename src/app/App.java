package app;

import model.*;
import action.*;
import game.*;
import ui.*;
import engine.*;
import java.util.*;


public class App {
    public static void main(String[] args) {
        
        // get input for the number of players etc
        InputValidator inputValidator = new InputValidator();
        ConsoleManager cManager = new ConsoleManager(inputValidator);

        // Initialize game state
            // initialize players -> initialize board -> initialize game state 

        int playerCount = cManager.promptForPlayerCount();
        Map<String, String> playerDetails = cManager.promptForPlayerDetails(playerCount);

        List<Player> players = new ArrayList<>();
        for (Map.Entry<String, String> entry : playerDetails.entrySet()) {
            String playerName = entry.getKey();
            String playerBirthdayString = entry.getValue(); // parse birthday
            int day = Integer.parseInt(playerBirthdayString.substring(0, 2));
            int month = Integer.parseInt(playerBirthdayString.substring(3, 5));
            int year = Integer.parseInt(playerBirthdayString.substring(6, 8));

            Birthday playerBirthday = new Birthday(day, month, year);
            // Initialize Player

            players.add(new Player(playerName, playerBirthday));
            
        }


        // need a function to sort players


        

        // console manager
        // game state
        // game
    
    }
}
