package snakegame;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientSender {
    private ObjectOutputStream objectOutputStream;
    private char dir;
    
    public ClientSender() {
        objectOutputStream = null;
    }

    public void sending(char dir){
        try {
                if(objectOutputStream != null){
                objectOutputStream.writeChar(dir);
                objectOutputStream.reset();
            }
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
