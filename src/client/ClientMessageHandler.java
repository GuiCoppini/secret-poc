package client;

import com.sun.javafx.binding.StringFormatter;
import gamecore.Player;
import gamecore.Table;
import java.io.Serializable;
import javafx.application.Platform;
import javax.swing.*;
import system.Message;

public class ClientMessageHandler {


    static void handleMessage(Message message, ChatClient chat, Interface anInterface) {

        switch (message.getCommand()) {
            case ("you"):
                Client.setId((int) message.getArguments().get(0));
                Client.closeInitialFrame();
                break;
            case ("player"):
                Client.setJogador((int) message.getArguments().get(0));
                break;
            case ("print"):
                String s = "";
                System.out.println();
                for (Serializable arg : message.getArguments()) {
                    System.out.println(arg);
                    s+= arg;
                }
                JOptionPane.showMessageDialog(null, s, "Connect4", JOptionPane.INFORMATION_MESSAGE);
                break;

            case ("play"):
                anInterface.setWaitingOponent(false);
                break;

            case ("gameStarted"):
                anInterface.setRedMove(Client.getId() == 1);
                Thread thread = new Thread(anInterface);
                thread.start();
                break;

            case ("table"):
                System.out.println("Updating table");
                Client.localTable = (Table) message.getArguments().get(0);
                break;

            case ("update"):
                int column = (int) message.getArguments().get(0);
                int playerId = (int) message.getArguments().get(1);

                System.out.println("Recebeu msg na coluna " + column);
                Client.localTable.add(column, playerId);

                for (int i = 0; i < Client.localTable.getTable().length; i++) {
                    for (int j = 0; j < Client.localTable.getTable()[i].length; j++) {
                        System.out.print(Client.localTable.getTable()[i][j] + " ");
                    }
                    System.out.println();
                }

                break;

            case ("placeDisk"):
                int column1 = (int) message.getArguments().get(0);
                int playerId1 = (int) message.getArguments().get(1);
                Platform.runLater(() -> anInterface.placeDisc(new Interface.Disc(playerId1 == 1), column1, false));
                break;

            case ("printtable"):
                for (int i = 0; i < Client.localTable.getTable().length; i++) {
                    for (int j = 0; j < Client.localTable.getTable()[i].length; j++) {
                        System.out.print(Client.localTable.getTable()[i][j] + " ");
                    }
                }

                Thread thread1 = new Thread(anInterface);
                thread1.start();

                JOptionPane.showMessageDialog(null,
                        "Vermelho: " + message.getArguments().get(0) + "\n Amarelo: " + message.getArguments().get(1), "Connect4",
                        JOptionPane.INFORMATION_MESSAGE);

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                for (int j = 0; j < Client.localTable.getTable()[0].length; j++) {
                    for (int i = Client.localTable.getTable().length - 1; i >= 0; i--) {
                        if (Client.localTable.getTable()[i][j] > 0) {
                            final int linha = i;
                            final int coluna = j;
                            Platform.runLater(() -> anInterface.placeDisc(new Interface.Disc(Client.localTable.getTable()[linha][coluna] == 1), coluna, false));
                        }
                    }
                }
                System.out.println();

                break;

            case ("winner"):
                int winnerId = (int) message.getArguments().get(0);

                if (Client.getId() == winnerId) {
                    anInterface.setWaitingOponent(true);
                    JOptionPane.showMessageDialog(null, "YOU WON!", "Connect4", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("YOU WON!");
                } else {
                    anInterface.setWaitingOponent(true);
                    JOptionPane.showMessageDialog(null, "YOU LOST!", "Connect4", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("YOU LOST!");
                }
                break;

            case ("someonewon"):
                String winnerName = (String) message.getArguments().get(0);
                System.out.println(winnerName + " won");
                JOptionPane.showMessageDialog(null, winnerName + " won", "Connect4", JOptionPane.INFORMATION_MESSAGE);
                break;

            case ("chat"):
                Player player = (Player) message.getArguments().get(0);
                String text = (String) message.getArguments().get(1);

                chat.sendMessage(player.getName(), text);
                break;

            case ("not-your-turn"):
                System.out.println("Ops, nao eh sua vez de jogar, voce jogou na vez errada.");
        }
    }

    private static Integer getOponent(Integer jogador) {
        if (jogador == null) return null;
        if (jogador == 1) return 2;
        return 1;
    }
}
