package client;

import java.net.Socket;
import java.util.Scanner;

import gamecore.Player;
import gamecore.Table;
import system.Connection;
import system.Message;
import static java.lang.System.in;

public class Client {
    static int id;
    static Scanner scanner = new Scanner(in);
    static Connection connection;
    static Table localTable = new Table();

    private static void connect(String ip, int port) {
        try {
            connection = new Connection(new Socket(ip, port));
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        System.out.println("Insira seu nick");
        String name = scanner.nextLine();

        connect("localhost", 5555);
        System.out.println("Connected to server");

        connection.sendMessage(new Message("login", name));

        new Thread(() -> {
            while(true)
                ClientMessageHandler.handleMessage(connection.readMessage());
        }).start();

        KeepAliveSender sender = new KeepAliveSender();

        Thread thread = new Thread(sender);
        thread.start();

    }

    public static void play() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insira uma coluna");
        int column = sc.nextInt();

        Client.connection.sendMessage(new Message("add", column-1));
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Client.id = id;
    }
}
