package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private static ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5555);

            while(true) {
                System.out.println("Waiting connection");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                Thread thread = new Thread(new ClientConnection(clientSocket));
                thread.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
