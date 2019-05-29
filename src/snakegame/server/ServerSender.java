package snakegame.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.server.ServerWindow;
import snakegame.element.Snake;

public class ServerSender{
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ServerWindow serverWindow;

    private Snake snake;
    
    public ServerSender(Socket socket, ServerWindow serverWindow, Snake snake){
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverWindow = serverWindow;
        this.snake = snake;
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

            serverWindow.snakes.remove(snake);
            serverWindow.playerSockets.remove(socket);
            serverWindow.serverSenders.remove(this);
            serverWindow.curPlayer--;

            try {
                objectInputStream.close();
                objectOutputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
