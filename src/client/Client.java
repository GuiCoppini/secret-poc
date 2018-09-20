package client;

import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.Scanner;

import gamecore.Table;
import static java.lang.System.in;
import system.Connection;
import system.Message;

public class Client {
    static ChatClient chat;
    static int id;
    static Scanner scanner = new Scanner(in);
    static Connection connection;
    static Table localTable = new Table();

    private static void connect(String ip, int port) {
        try {
            connection = new Connection(new Socket(ip, port));
        } catch(Exception e) {

        }
    }

    public static void main(String[] args) {
        System.out.println("Insira seu nick [ENTER para 'Player']");
        String name = scanner.nextLine();
        if(isBlank(name)) {
            name = "Player";
        }

        System.out.println("Insira o IP do servidor: [ENTER para localhost]");
        String serverIP = scanner.nextLine();
        if(isBlank(serverIP)) {
            serverIP = "localhost";
        }

        System.out.println("Insira a porta do servidor: [ENTER para 5555]");
        String port = scanner.nextLine();
        if(isBlank(port)) {
            port = "5555";
        } else if(!isNumeric(port)) {
            System.out.println("Valor da porta invalido, usando 5555.");
            port = "5555";
        }

        connect(serverIP, Integer.valueOf(port));
        System.out.println("Conectado ao servidor do jogo [TCP]");

        chat = new ChatClient(serverIP);

        System.out.println("Digite start para jogar ou watch para assistir");
        String command = scanner.nextLine();
        if(command.equals("start")) {
            connection.sendMessage(new Message("login", name));
        } else if(command.equals("watch")) {
            connection.sendMessage(new Message("watch", name));
        } else {
            System.out.println("Comando invalido.");
        }

        new Thread(() -> {
            while(true) {
                try {
                    ClientMessageHandler.handleMessage(connection.readMessage());
                } catch(SocketException e) {
                    System.out.println("O servidor esta offline. Partida encerrada.");
                    System.exit(0);
                }
            }
        }).start();

        ChatClient sender = new ChatClient("localhost");

        Thread thread = new Thread(sender);
        thread.start();

        while(true) { // le comando do player
            handleInput(scanner.nextLine());
        }
    }

    private static boolean isBlank(String serverIP) {
        return serverIP == null || Objects.equals(serverIP, "") || Objects.equals(serverIP, "\n");
    }

    private static void handleInput(String command) {
        if(!Objects.equals(command, "") && !Objects.equals(command, "\n")) {
            if('/' == command.charAt(0)) { // comecou com / eh msg de chat
                chat.sendChat(command.substring(1));
            } else { // deve ser jogada
                if(isNumeric(command) && (Integer.valueOf(command) - 1) <= 7) { // ve se eh um numero e se cabe nas colunas
                    int column = Integer.valueOf(command) - 1;
                    connection.sendMessage(new Message("add", column));
                } else { // jogada invalida
                    System.out.println("Comando invalido.");
                }
            }
        }
    }

    private static boolean isNumeric(String command) {
        try {
            Integer.valueOf(command);
            return true;
        } catch(Exception e) {
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
