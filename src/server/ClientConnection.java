package server;

import gamecore.Player;
import system.Connection;
import system.Message;

import java.net.Socket;
import java.net.SocketException;

public class ClientConnection implements Runnable {

    private Connection connection;

    protected ClientConnection(Socket client) {
        this.connection = new Connection(client);
    }

    @Override
    public void run() {

        while (true) {
            try {
                ServerMessageHandler
                        .handleIncomingMessage(connection.readMessage(), this);
            } catch (SocketException e) {
                System.out.println("Um player se desconectou, avisando todo mundo.");
                Player offlinePlayer = MainThread.findPlayerByClientConnection(this);
                MainThread.players.remove(offlinePlayer);
                MainThread.broadcastToClients(new Message("print", "Algum player se desconectou, encerrando server."));

                System.exit(0);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
