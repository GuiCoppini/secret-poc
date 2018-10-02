package client;

import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.swing.*;

public class ChatClient implements Runnable {
    private String serverIP;

    static JFrame newFrame = new JFrame();
    static JButton sendMessage;
    static JTextField messageBox;
    static JTextArea chatBox;
    static String username;

    public ChatClient(String serverIP) {
        this.serverIP = serverIP;
    }

    @Override
    public void run() {

    }

    public void sendChat(String message) {
        String messageWithId = Client.getId()+","+message;
        DatagramSocket clientSocket;
        try {
            clientSocket = new DatagramSocket();

            DatagramPacket sendPacket = new DatagramPacket(messageWithId.getBytes(), messageWithId.getBytes().length, InetAddress.getByName(serverIP), 4555);
            clientSocket.setBroadcast(true);
            clientSocket.send(sendPacket);
        } catch (SocketException e) {
            System.out.println("Algo errado ocorreu ao estabelecer um DatagramSocket.");
            System.exit(0);
        } catch (
                Exception e) {
            System.out.println("Nao foi possivel encontrar o host de chat" + serverIP);
            System.exit(0);
        }
    }

    public static void runChat(String userName) {
        username = userName;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(e -> {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText()
                        + "\n");
                messageBox.setText("");
            }
            messageBox.requestFocusInWindow();

        });

        chatBox = new

                JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new

                Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new

                JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new

                Insets(0, 10, 0, 0);

        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);

    }


}

