package server;

import java.util.HashMap;
import java.util.Map;

import gamecore.Player;
import gamecore.Room;
import system.Message;

public class MainThread {
    private static Server server;
    protected static Room room = new Room();

    // map de players por ID
    protected static Map<Player, ClientConnection> players = new HashMap<>();

    public static void main(String[] args) {
        runServerSocket();
    }

    private static void runServerSocket() {
        server = new Server();

        Thread thread = new Thread(server);
        thread.start();
    }

    public static void broadcastToClients(Message message) {

        for(Player player : players.keySet()) {
            System.out.println("Sending message to player of ID=" + player.getId());

            players.get(player).getConnection().sendMessage(message);
        }
    }
}
