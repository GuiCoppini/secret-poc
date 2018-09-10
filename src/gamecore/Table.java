package gamecore;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Table implements Serializable {

    private AtomicInteger[][] table = new AtomicInteger[7][7];

    public Table() {
        for (int i = 0; i<table.length; i++) {
            for(int j = 0; j<table[i].length; j++)
                table[i][j] = new AtomicInteger(0);
        }
    }

    public AtomicInteger[][] add(int column, int player) { //player pra mostrar quem jogou
        if(player == 0) throw new RuntimeException("Player nao pode ser 0");

        for(int line = table.length-1; line >= 0; line--) {
            if(table[line][column].get() == 0) { // linha livre
                table[line][column] = new AtomicInteger(player);
                break;
            }
        }
        return table;
    }

    public AtomicInteger[][] getTable() {
        return table;
    }
}
