package snakegame.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.server.ServerWindow;

public class ServerSender{
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ServerWindow serverWindow;
    
    public ServerSender(Socket socket, ServerWindow serverWindow){
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverWindow = serverWindow;
    }

    public void sending(){
        try {
            if(objectOutputStream != null){
                objectOutputStream.writeInt(serverWindow.curPlayer);
                for(int i = 0; i < serverWindow.curPlayer; i++){
                    objectOutputStream.writeObject(serverWindow.snakes.get(i));
                }
                objectOutputStream.writeObject(serverWindow.apple);
                objectOutputStream.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
