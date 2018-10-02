package server;

import gamecore.Player;
import gamecore.Room;
import java.util.Arrays;
import system.Message;

public class ServerMessageHandler {

    static void handleIncomingMessage(Message message, ClientConnection c) {
        Room room = MainThread.room;
        switch (message.getCommand()) {
            case ("add"):

                Player player = null;
                player = MainThread.findPlayerByClientConnection(c);

                System.out.println("Actual: " + room.getActualPlayer().getId());

                if (room.getActualPlayer().getId() != player.getId()) {
                    System.out.println("Player " + player.getId() + " jogou na vez errada.");
                    c.getConnection().sendMessage(new Message("not-your-turn"));
                    return;
                }

                int column = (int) message.getArguments().get(0);
                System.out.println("(" + player.getName() + ")" + " Column = " + column);
                System.out.println("By player " + player.getName());
                room.getTable().add(column, player.getId());

                room.changePlayer(); // troca vez

                MainThread.broadcastToClients(new Message("update", column, player.getId()));
                MainThread.broadcastToWatchers(new Message("update", column, player.getId()));

                int winnerId = room.verificaVencedor(room.getPlayers()[0].getId(), room.getPlayers()[1].getId());
                System.out.println("WINNER ID = " + winnerId);
                if (winnerId != 0) {
                    Message winnerMessage = new Message("winner", winnerId);
                    MainThread.broadcastToClients(winnerMessage);
                    MainThread.broadcastToWatchers(new Message("someonewon", Arrays.stream(room.getPlayers()).filter(p -> p.getId().equals(winnerId)).findFirst().get().getName()));
                } else {
                    MainThread.sendPlay(room.getActualPlayer());
                }

                break;
            case ("login"):
                String nickname = (String) message.getArguments().get(0);
                Player joined = new Player(nickname);
                MainThread.players.put(joined, c);
                room.addPlayer(joined);

                MainThread.broadcastToClients(new Message("print", "Player " + nickname + " joined."));

                c.getConnection().sendMessage(new Message("you", joined.getId()));
                c.getConnection().sendMessage(new Message("table", room.getTable()));
                break;
            case ("watch"):
                String watcherNickname = (String) message.getArguments().get(0);
                Player watcherJoined = new Player(watcherNickname);
                MainThread.watchers.put(watcherJoined, c);
                room.addWatcher(watcherJoined);

                MainThread.broadcastToClients(new Message("print", watcherNickname + " is watching."));
                c.getConnection().sendMessage(new Message("you", watcherJoined.getId()));
                c.getConnection().sendMessage(new Message("table", room.getTable()));
                c.getConnection().sendMessage(new Message("printtable"));
        }
    }
}
