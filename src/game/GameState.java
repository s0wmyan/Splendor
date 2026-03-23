package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.TurnManager;
import model.Bank;
import model.Board;
import model.Player;

public class GameState {

    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex; // store under game state
    // private TurnManager turnManager;

    public GameState(Board board, List<Player> players) {
        this.board = board;
        this.players = new ArrayList<>(players);
        // this.turnManager = turnManager;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Player getCurrentPlayer() {
        return this.getPlayers().get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Bank getBank(){  
        return board.getBank();
    }

    public void setPlayerIndex(int index) {
        currentPlayerIndex = index;
    }
}

