package game;

import model.Board;
import model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {

    private final Board board;
    private final List<Player> players;

    public GameState(Board board, List<Player> players) {
        this.board = board;
        this.players = new ArrayList<>(players);
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
}

