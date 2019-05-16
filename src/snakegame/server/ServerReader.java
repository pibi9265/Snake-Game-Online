package snakegame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.server.ServerWindow;
import snakegame.element.snake.*;

public class ServerReader implements Runnable {
    private ServerWindow serverWindow;
    private Snake snake;
    private ObjectInputStream objectInputStream;
    boolean stop;

    public ServerReader(ServerWindow serverWindow, Snake snake) {
        this.serverWindow = serverWindow;
        this.snake = snake;
        objectInputStream = null;
        stop = false;
    }

    @Override
    public void run() {
        while(!stop){
            try {
                char dir;
                dir = objectInputStream.readChar();
                serverWindow.setDir(snake, dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSocket(Socket socket) {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void threadStop(){
        stop = true;
    }
}
