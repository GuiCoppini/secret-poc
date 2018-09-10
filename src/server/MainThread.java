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

        while(checkLength(room.getPlayers()) < 2) {
            // faz nada
        }

        System.out.println("passou");
        sleep(1000);

        waitForPlay(room.getActualPlayer());
    }

    public static void waitForPlay(Player actualPlayer) {
        ClientConnection playerConnection = players.get(actualPlayer);
        Message playMessage = new Message("play");

        playerConnection.getConnection().sendMessage(playMessage);
    }

    private static int checkLength(Player[] array) {
        int length = 0;

        for(int i = 0; i<array.length; i++) {
            if(array[i] != null) length++;
        }
        return length;
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

    private static void sleep(int millis) {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
