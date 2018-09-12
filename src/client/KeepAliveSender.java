package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KeepAliveSender implements Runnable {
    @Override
    public void run() {
//        try {
//            while(true) {
//                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//                DatagramSocket clientSocket = new DatagramSocket();
//                InetAddress IPAddress = InetAddress.getByName("localhost");
//                byte[] sendData = new byte[1024];
//                String sentence = "I AM ALIVE";
//                sendData = sentence.getBytes();
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 4555);
//                System.out.println("TO SERVER:" + sendPacket);
//                clientSocket.send(sendPacket);
//                Thread.sleep(1000);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
    }
}
