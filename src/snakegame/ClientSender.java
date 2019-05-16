package snakegame;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSender {
    private ObjectOutputStream objectOutputStream;

    public ClientSender() {
        objectOutputStream = null;
    }

    public void sending(char dir){
        try {
            objectOutputStream.writeChar(dir);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
