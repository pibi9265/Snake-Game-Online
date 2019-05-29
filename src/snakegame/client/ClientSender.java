/*
package snakegame.client;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientSender {
    private ObjectOutputStream objectOutputStream;
    
    public ClientSender(Socket socket) {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            objectOutputStream = null;
        }
    }

    public void sending(char dir){
        try {
            if(objectOutputStream != null){
            	System.out.println("Try Sending Direction");
                objectOutputStream.writeChar(dir);
                objectOutputStream.reset();
            }
            System.out.println("Sending Direction Complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset(){
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
