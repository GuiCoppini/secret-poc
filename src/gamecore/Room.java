package gamecore;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private Table table = new Table();
    private List<Player> players = new ArrayList<>();
    private Player actualPlayer;

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Table getTable() {
        return table;
    }

    public void addPlayer(Player joined) {
        players.add(joined);
        if(actualPlayer == null) actualPlayer = joined;

    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }
}

