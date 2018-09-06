package server;

import gamecore.Player;
import system.Message;

public class ServerMessageHandler {

    static void handleIncomingMessage(Message message, ClientConnection c) {
        switch(message.getCommand()) {
            case ("coordinates"):
                int x = (int) message.getArguments().get(0);
                int y = (int) message.getArguments().get(1);
                System.out.println("X = " + x + " | Y = " + y);
                MainThread.room.getTable().increment(x, y);

                MainThread.broadcastToClients(new Message("inc", x,y));
                break;
            case ("login"):
                String nickname = (String) message.getArguments().get(0);
                Player joined = new Player(nickname);
                MainThread.players.put(joined.getId(), c);

//                c.getConnection().sendMessage(new Message("play"));

                MainThread.broadcastToClients(new Message("print","Player " + nickname + " joined."));
                c.getConnection().sendMessage(new Message("table",MainThread.room.getTable()));
        }
    }
}
