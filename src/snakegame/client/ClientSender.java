package snakegame.client;

import java.net.Socket;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSender {
    private BufferedOutputStream bufferedOutputStream;
    private ObjectOutputStream objectOutputStream;

    ClientSender() {
        bufferedOutputStream = null;
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
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
