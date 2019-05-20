package snakegame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import snakegame.ServerWindow;
import snakegame.Snake;

public class ServerSender{
    private ServerWindow serverWindow;
    private Snake snake;
    private ObjectOutputStream objectOutputStream;

    public ServerSender(ServerWindow serverWindow, Snake snake){
        this.serverWindow = serverWindow;
        this.snake = snake;
        objectOutputStream = null;
    }

    public void sending(){
        try {
            if(objectOutputStream != null){
                objectOutputStream.writeObject(snake);
                objectOutputStream.writeObject(serverWindow.apple);
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
