package server;

import java.util.HashMap;
import java.util.Map;

import gamecore.Player;
import gamecore.Room;
import system.Message;

public class MainThread {
    private static Server server;
    private static ChatServer listener;
    protected static Room room = new Room();

    // map de players por ID
    protected static Map<Player, ClientConnection> players = new HashMap<>();

    // map de watchers por ID
    protected static Map<Player, ClientConnection> watchers = new HashMap<>();

    public static void main(String[] args) {
        runServerSocket();
        runChatSocket();

        while (checkLength(room.getPlayers()) < 2) {
            // faz nada
        }

        System.out.println("Ambos os jogadores se conectaram");
        sleep(2000);

        sendPlay(room.getActualPlayer());
    }

    public static void sendPlay(Player actualPlayer) {
        ClientConnection playerConnection = players.get(actualPlayer);
        Message playMessage = new Message("play");

        playerConnection.getConnection().sendMessage(playMessage);
    }

    private static int checkLength(Player[] array) {
        int length = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) length++;
        }
        return length;
    }

    private static void runServerSocket() {
        server = new Server();

        Thread thread = new Thread(server);
        thread.start();
    }

    private static void runChatSocket() {
        listener = new ChatServer();

        Thread thread = new Thread(listener);
        thread.start();
    }

    public static void broadcastToClients(Message message) {

        for (Player player : players.keySet()) {
            System.out.println("Sending message to player of ID=" + player.getId());

            players.get(player).getConnection().sendMessage(message);
        }
    }

    public static void broadcastToWatchers(Message message) {
        for (Player watcher : watchers.keySet()) {
            System.out.println("Sending message to watcher of ID=" + watcher.getId());

            watchers.get(watcher).getConnection().sendMessage(message);
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Player findPlayerById(int id) {
        for (Player p : players.keySet()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static Player findWatcherById(int id) {
        for (Player w : watchers.keySet()) {
            if (w.getId() == id) {
                return w;
            }
        }
        return null;
    }

    public static Player findPlayerByClientConnection(ClientConnection c) {
        for (Map.Entry<Player, ClientConnection> entry : MainThread.players.entrySet()) {
            if (entry.getValue().equals(c)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
