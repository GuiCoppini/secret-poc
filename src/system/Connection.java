package system;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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

    public Message readMessage() throws SocketException {
        Message input;
        try {
            while (true)
                if ((input = (Message) in.readObject()) != null) {
                    return input;
                }
        } catch (EOFException | SocketException e) {
            throw new SocketException("Someone went offline.");
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
