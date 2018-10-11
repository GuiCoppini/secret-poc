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
    private static boolean watching;
    private static Interface anInterface;
    private static Integer jogador;

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

        connect(ipField.getText(), Integer.valueOf(portField.getText()));
        System.out.println("Conectado ao servidor do jogo [TCP]");

        JButton botaoIniciar = new JButton("Iniciar jogo");
        botaoIniciar.addActionListener(e -> {
            connection.sendMessage(new Message("login", playerNameField.getText()));
            JOptionPane.showMessageDialog(null, "Aguardando oponente", "Connect4", JOptionPane.INFORMATION_MESSAGE);
            anInterface = new Interface(false, connection);
            chat(ipField, playerNameField, anInterface);
        });
        painel.add(botaoIniciar);

        JButton botaoAssistir = new JButton("Assistir jogo");
        botaoAssistir.addActionListener(e -> {
            connection.sendMessage(new Message("watch", playerNameField.getText()));
            anInterface = new Interface(false, connection);
            chat(ipField, playerNameField, anInterface);
        });
        painel.add(botaoAssistir);

        JFrame janela = new JFrame("Tela Inicial");
        janela.add(painel);

        janela.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        janela.pack();
        janela.setVisible(true);

        telaInicial = janela;
    }

    private static void chat(JTextField ipField, JTextField playerNameField, Interface anInterface) {
        chat = new ChatClient(ipField.getText());
        chat.runChat(playerNameField.getText());

        new Thread(() -> {
            while (true)
                try {
                    ClientMessageHandler.handleMessage(connection.readMessage(), chat, anInterface);
                } catch (SocketException e) {
                    System.out.println("O servidor esta offline. Partida encerrada.");
                    System.exit(0);
                }
        }).start();

        ChatClient sender = new ChatClient("localhost");

        Thread thread = new Thread(sender);
        thread.start();
    }

    private static boolean isNumeric(String command) {
        try {
            Integer.valueOf(command);
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public static void setWatching(boolean w) {
        watching = w;
    }

    public static Integer getJogador() {
        return jogador;
    }

    public static void setJogador(int j) {
        jogador = j;
    }
}
