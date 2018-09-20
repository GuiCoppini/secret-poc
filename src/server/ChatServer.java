package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import gamecore.Player;
import system.Message;

public class ChatServer implements Runnable {
    @Override
    public void run() {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(4555);
            serverSocket.setBroadcast(true);

            byte[] receiveData = new byte[1024];
            while(true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData(), 0, receivePacket.getLength() );

                // primeiro char eh o ID do player q enviou

                int id = Integer.valueOf(sentence.charAt(0) + ""); // sim, tem q fazer isso
                Player playerWhoSent = MainThread.findPlayerById(id);

                if (playerWhoSent == null) {
                    // eh watcher
                    System.out.println("Spectator mandou msg mas nem devia");
                    return;
                }
                System.out.println(playerWhoSent.getName() + ": " + sentence.substring(1));
                MainThread.broadcastToClients(new Message("chat", playerWhoSent, sentence.substring(1)));
                MainThread.broadcastToWatchers(new Message("chat", playerWhoSent, sentence.substring(1)));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
