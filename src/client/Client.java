package client;

import java.net.Socket;
import java.util.Scanner;

import gamecore.Table;
import system.Connection;
import system.Message;
import static java.lang.System.in;

public class Client {
    static ChatClient chat;
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
        chat = new ChatClient("localhost");
        System.out.println("Connected to server");


        connection.sendMessage(new Message("login", name));

        new Thread(() -> {
            while(true)
                ClientMessageHandler.handleMessage(connection.readMessage());
        }).start();

        ChatClient sender = new ChatClient("localhost");

        Thread thread = new Thread(sender);
        thread.start();

        String command;
        while(true) {
            command = scanner.nextLine();

            if('/' == command.charAt(0)) { // eh msg de chat
                chat.sendChat(command.substring(1));
            } else { // deve ser jogada
                if (isValidInput(command)) {
                    int column = Integer.valueOf(command) - 1;
                    connection.sendMessage(new Message("add", column));
                } else { // jogada invalida
                    System.out.println("Comando invalido.");
                }
            }
        }

    }

    private static boolean isValidInput(String command) {
        boolean isValid = false;
        try {
            Integer.valueOf(command);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void play() {
        System.out.println("Sua vez de jogar! Insira uma coluna [1-7]");
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Client.id = id;
    }
}
