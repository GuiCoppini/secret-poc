package server;

import java.util.Map;

import gamecore.Player;
import sun.applet.Main;
import system.Message;

public class ServerMessageHandler {

    static void handleIncomingMessage(Message message, ClientConnection c) {
        switch(message.getCommand()) {
            case ("add"):

                Player player = null;
                for (Map.Entry<Player, ClientConnection> entry : MainThread.players.entrySet()) {
                    if (entry.getValue().equals(c)) {
                        player = entry.getKey();
                    }
                }

                System.out.println("Actual: " + MainThread.room.getActualPlayer().getId());
                System.out.println("Who played: " + player.getId());

                if(MainThread.room.getActualPlayer().getId() != player.getId()) {
                    System.out.println("WRONG TURN PLAY");
                    return;
                }

                int column = (int) message.getArguments().get(0);
                System.out.println("(" + player.getName() + ")" +  " Column = " + column);
                System.out.println("By player " + player.getName());
                MainThread.room.getTable().add(column, player.getId());

                MainThread.room.changePlayer(); // troca vez

                MainThread.broadcastToClients(new Message("update", column, player.getId()));

                MainThread.waitForPlay(MainThread.room.getActualPlayer());

                break;
            case ("login"):
                String nickname = (String) message.getArguments().get(0);
                Player joined = new Player(nickname);
                MainThread.players.put(joined, c);
                MainThread.room.addPlayer(joined);

                MainThread.broadcastToClients(new Message("print","Player " + nickname + " joined."));
                c.getConnection().sendMessage(new Message("table",MainThread.room.getTable()));
        }
    }
}
