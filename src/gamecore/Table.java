package gamecore;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Table implements Serializable {

    private AtomicInteger[][] table = new AtomicInteger[5][5];

    public Table() {
        for (int i = 0; i<table.length; i++) {
            for(int j = 0; j<table[i].length; j++)
                table[i][j] = new AtomicInteger(0);
        }
    }

    public AtomicInteger[][] increment(int x, int y) {
        table[x-1][y-1].incrementAndGet();
        return table;
    }

    public AtomicInteger[][] getTable() {
        return table;
    }
}
