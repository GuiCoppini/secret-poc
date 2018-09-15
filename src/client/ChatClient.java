package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ChatClient implements Runnable {
    private String serverIP = "localhost";
    public ChatClient(String serverIP) {
        this.serverIP = serverIP;
    }

    @Override
    public void run() {
    }

    public void sendChat(String message) {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Algo errado ocorreu ao estabelecer um DatagramSocket.");
            System.exit(0);
        }
        byte[] sendData = message.getBytes();
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverIP), 4555);
            clientSocket.send(sendPacket);
        } catch (Exception e) {
            System.out.println("Nao foi possivel encontrar o host de chat" + serverIP);
            System.exit(0);
        }
    }
}
