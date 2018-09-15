package system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message readMessage() {
        Message input;
        try {
            while (true)
                if ((input = (Message) in.readObject()) != null) {
                    return input;
                }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Algo deu errado e quebrou tudo");
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
