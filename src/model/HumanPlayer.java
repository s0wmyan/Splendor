package model;

import action.*;
import game.*;
import ui.*;

public class HumanPlayer extends Player {

    // Constructor
    public HumanPlayer(String name, Birthday birthday) {
        super(name, birthday);
    }
    
    @Override
    public Action takeTurn(GameState gameState) {
        
        // Player's turn. Board state provided.
        // engine uses an 'instanceof' check and bypasses this method entirely for humans.
        // return null, engine will use promptAction
        
        return null;
    }
}