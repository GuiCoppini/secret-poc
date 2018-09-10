package gamecore;

import java.io.Serializable;

public class Table implements Serializable {

    private int[][] table = new int[7][7];

    public Table() {
        for (int i = 0; i<table.length; i++) {
            for(int j = 0; j<table[i].length; j++)
                table[i][j] = 0;
        }
    }

    public int[][] add(int column, int player) { //player pra mostrar quem jogou
        if(player == 0) throw new RuntimeException("Player nao pode ser 0");

        for(int line = table.length-1; line >= 0; line--) {
            if(table[line][column] == 0) { // linha livre
                table[line][column] = player;
                break;
            }
        }
        return table;
    }

    public int[][] getTable() {
        return table;
    }
}
