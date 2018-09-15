package server;

import sun.font.EAttribute;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Server implements Runnable {

    private static ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Escolha a porta do servidor: [ENTER para 5555]");
            String port = sc.nextLine();
            if(port != null && !Objects.equals(port, "") && !Objects.equals(port, "\n")) {
                try {
                    serverSocket = new ServerSocket(Integer.valueOf(port));
                } catch (Exception e) {
                    System.out.println("Porta invalida.");
                }
            } else {
                serverSocket = new ServerSocket(5555);
            }

            while(true) {
                System.out.println("Aguardando players.");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Algum cliente se conectou");

                Thread thread = new Thread(new ClientConnection(clientSocket));
                thread.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
