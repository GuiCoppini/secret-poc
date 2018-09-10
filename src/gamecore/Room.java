package gamecore;

import system.Message;

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

    public int verificaVencedor(int player1, int player2) {

        int sequenciaCliente1 = 0;
        int sequenciaCliente2 = 0;
        boolean empate = true;
        /*
         * Verifica ganhador horizontal
         */

        for (int linha = 6; linha >= 0; linha--) {
            for (int coluna = 6; coluna >= 0; coluna--) {
                if (table.getTable()[linha][coluna] == player1) {
                    sequenciaCliente1++;
                    sequenciaCliente2 = 0;
                    if (sequenciaCliente1 == 4) {
                        return player1;
                    }
                }

                if (table.getTable()[linha][coluna] == player2) {
                    sequenciaCliente2++;
                    sequenciaCliente1 = 0;
                    if (sequenciaCliente2 == 4) {
                        return player2;
                    }
                }

                if (table.getTable()[linha][coluna] == 0) {
                    empate = false;
                    sequenciaCliente1 = 0;
                    sequenciaCliente2 = 0;
                }
            }
            sequenciaCliente1 = 0;
            sequenciaCliente2 = 0;
        }


        /*
         * Verifica ganhador vertical
         */

        for (int coluna = 6; coluna >= 0; coluna--) {
            for (int linha = 6; linha >= 0; linha--) {
                if (table.getTable()[linha][coluna] == player1) {
                    sequenciaCliente1++;
                    sequenciaCliente2 = 0;
                    if (sequenciaCliente1 == 4) {
                        return player1;
                    }
                }

                if (table.getTable()[linha][coluna] == player2) {
                    sequenciaCliente2++;
                    sequenciaCliente1 = 0;
                    if (sequenciaCliente2 == 4) {
                        return player2;
                    }
                }

                if (table.getTable()[linha][coluna] == 0) {
                    empate = false;
                    sequenciaCliente1 = 0;
                    sequenciaCliente2 = 0;
                }
            }
            sequenciaCliente1 = 0;
            sequenciaCliente2 = 0;
        }

        /**
         * Verifica ganhador diagonal
         */

        int vencedorDiagonal;

        for (int coluna = 0; coluna < 7; coluna++) {
            vencedorDiagonal = verificaDiagonal1(coluna, 0, player1, player2);

            if (vencedorDiagonal != 0) {
                return vencedorDiagonal;
            }
        }

        for (int linha = 1; linha < 6; linha++) {
            vencedorDiagonal = verificaDiagonal1(0, linha, player1, player2);

            if (vencedorDiagonal != 0) {
                return vencedorDiagonal;
            }

            vencedorDiagonal = verificaDiagonal2(0, linha, player1, player2);

            if (vencedorDiagonal != 0) {
                return vencedorDiagonal;
            }
        }

        for (int coluna = 1; coluna < 7; coluna++) {

            vencedorDiagonal = verificaDiagonal2(coluna, 6, player1, player2);

            if (vencedorDiagonal != 0) {
                return vencedorDiagonal;
            }
        }

        if (empate) {
            return -1;
        }

        return 0;

    }

    private int verificaDiagonal2(int coluna, int linha, int player1, int player2) {

        int sequenciaCliente1 = 0;
        int sequenciaCliente2 = 0;
        while (coluna <= 6 && linha <= 6 && linha >= 0 && coluna >= 0) {

            if (table.getTable()[linha][coluna] == player1) {
                sequenciaCliente1++;
                sequenciaCliente2 = 0;
                if (sequenciaCliente1 == 4) {
                    return player1;
                }
            }

            if (table.getTable()[linha][coluna] == player2) {
                sequenciaCliente2++;
                sequenciaCliente1 = 0;
                if (sequenciaCliente2 == 4) {
                    return player2;
                }
            }

            if (table.getTable()[linha][coluna] == 0) {
                sequenciaCliente1 = 0;
                sequenciaCliente2 = 0;
            }

            coluna++;
            linha--;
        }

        return 0;
    }

    private int verificaDiagonal1(int coluna, int linha, int player1, int player2) {

        int sequenciaCliente1 = 0;
        int sequenciaCliente2 = 0;
        while (coluna <= 6 && linha <= 6) {

            if (table.getTable()[linha][coluna] == player1) {
                sequenciaCliente1++;
                sequenciaCliente2 = 0;
                if (sequenciaCliente1 == 4) {
                    return player1;
                }
            }

            if (table.getTable()[linha][coluna] == player2) {
                sequenciaCliente2++;
                sequenciaCliente1 = 0;
                if (sequenciaCliente2 == 4) {
                    return player2;
                }
            }

            if (table.getTable()[linha][coluna] == 0) {
                sequenciaCliente1 = 0;
                sequenciaCliente2 = 0;
            }

            coluna++;
            linha++;
        }

        return 0;
    }
}

