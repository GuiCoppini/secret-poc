package server;

import java.util.Map;

import gamecore.Player;
import sun.applet.Main;
import system.Message;

public class ServerMessageHandler {

    static void handleIncomingMessage(Message message, ClientConnection c) {
        switch(message.getCommand()) {
            case ("add"):
                int column = (int) message.getArguments().get(0);
                System.out.println("Column = " + column);

                Player player = null;
                for (Map.Entry<Player, ClientConnection> entry : MainThread.players.entrySet()) {
                    if (entry.getValue().equals(c)) {
                        player = entry.getKey();
                    }
                }

                System.out.println("By player " + player.getName());
                MainThread.room.getTable().add(column, player.getId());

                MainThread.broadcastToClients(new Message("update", column, player.getId()));
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
