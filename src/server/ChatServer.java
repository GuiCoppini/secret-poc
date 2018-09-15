package server;

import sun.applet.Main;
import system.Message;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ChatServer implements Runnable {
    @Override
    public void run() {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(4555);

            byte[] receiveData = new byte[1024];
            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                System.out.println("CHAT: " + sentence);

                MainThread.broadcastToClients(new Message("chat", sentence));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
