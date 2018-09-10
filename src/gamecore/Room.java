package gamecore;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public static final int PLAYERS_SIZE = 2;
    private Table table = new Table();
    private Player[] players = new Player[PLAYERS_SIZE];
    private Player winner; // vai ser settado quando alguem ganhar

    private int actualPlayerPosition = 0; // posicao do player atual no array

    public void changePlayer() {
        if(actualPlayerPosition == 0) actualPlayerPosition = 1;
        else actualPlayerPosition = 0;

        System.out.println("Player position is now " + actualPlayerPosition);
    }

    public Player[] getPlayers() {
        Player[] response = new Player[PLAYERS_SIZE];

        // faz copia pra evitar q alguem edite
        for(int i = 0; i<PLAYERS_SIZE; i++)
            response[i] = players[i];

        return response;
    }

    public Table getTable() {
        return table;
    }

    public void addPlayer(Player joined) {
        for(int i = 0; i < players.length; i++) {
            if(players[i] == null) {
                players[i] = joined;
                return;
            }
        }
    }

    public Player getActualPlayer() {
        return players[actualPlayerPosition];
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}

