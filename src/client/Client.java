package client;

import gamecore.Table;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.*;
import system.Connection;
import system.Message;

import static java.lang.System.in;

public class Client {
    static ChatClient chat;
    static int id;
    static Scanner scanner = new Scanner(in);
    static Connection connection;
    static Table localTable = new Table();
    static JFrame telaInicial;

    private static void connect(String ip, int port) {
        try {
            connection = new Connection(new Socket(ip, port));
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {


        JPanel painel = new JPanel();

        JTextField playerNameField = new JTextField("Player", 10);
        painel.add(playerNameField);

        JTextField ipField = new JTextField("localhost", 10);
        painel.add(ipField);

        JTextField portField = new JTextField("5555", 4);
        painel.add(portField);

        JButton botaoIniciar = new JButton("Iniciar jogo");
        botaoIniciar.addActionListener(e -> {
            connection.sendMessage(new Message("login", playerNameField.getText()));
            JOptionPane.showMessageDialog(null, "Aguardando oponente", "Connect4", JOptionPane.INFORMATION_MESSAGE);
        });
        painel.add(botaoIniciar);

        JButton botaoAssistir = new JButton("Assistir jogo");
        botaoAssistir.addActionListener(e -> {
            connection.sendMessage(new Message("watch", playerNameField.getText()));
            JOptionPane.showMessageDialog(null, "Tentando encontrar jogo", "Connect4", JOptionPane.INFORMATION_MESSAGE);
        });
        painel.add(botaoAssistir);

        JFrame janela = new JFrame("Tela Inicial");
        janela.add(painel);

        janela.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        janela.pack();
        janela.setVisible(true);

        telaInicial = janela;

        connect(ipField.getText(), Integer.valueOf(portField.getText()));
        System.out.println("Conectado ao servidor do jogo [TCP]");

        chat = new ChatClient(ipField.getText());
        chat.runChat(playerNameField.getText());

        new Thread(() -> {
            while (true)
                try {
                    ClientMessageHandler.handleMessage(connection.readMessage());
                } catch (SocketException e) {
                    System.out.println("O servidor esta offline. Partida encerrada.");
                    System.exit(0);
                }
        }).start();

        ChatClient sender = new ChatClient("localhost");

        Thread thread = new Thread(sender);
        thread.start();

        while (true) { // le comando do player
            handleInput(scanner.nextLine());
        }

    }

    private static void handleInput(String command) {
        if (!Objects.equals(command, "") && !Objects.equals(command, "\n")) {
            if ('/' == command.charAt(0)) { // comecou com / eh msg de chat
                chat.sendChat(command.substring(1));
            } else { // deve ser jogada
                if (isNumeric(command) && (Integer.valueOf(command) - 1) <= 7) { // ve se eh um numero e se cabe nas colunas
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

    public static void closeInitialFrame() {
        telaInicial.dispatchEvent(new WindowEvent(telaInicial, WindowEvent.WINDOW_CLOSING));

    }
}
