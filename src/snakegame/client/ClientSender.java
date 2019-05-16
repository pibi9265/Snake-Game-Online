package snakegame.client;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSender {
    private ObjectOutputStream objectOutputStream;

    ClientSender() {
        objectOutputStream = null;
    }

    public void sending(char dir){
        try {
            objectOutputStream.writeChar(dir);
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
