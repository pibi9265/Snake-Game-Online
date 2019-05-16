package snakegame.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import snakegame.server.ServerWindow;
import snakegame.element.snake.*;

public class ServerSender{
    private ServerWindow serverWindow;
    private Snake snake;
    private BufferedOutputStream bufferedOutputStream;
    private ObjectOutputStream objectOutputStream;

    public ServerSender(ServerWindow serverWindow, Snake snake){
        this.serverWindow = serverWindow;
        this.snake = snake;
        bufferedOutputStream = null;
        objectOutputStream = null;
    }

    public void sending(){
        try {
            objectOutputStream.writeObject(snake);
            objectOutputStream.writeObject(serverWindow.apple);
            objectOutputStream.flush();
            objectOutputStream.reset();
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
