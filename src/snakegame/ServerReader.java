package snakegame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.ServerWindow;
import snakegame.Snake;

public class ServerReader implements Runnable {
    private ServerWindow serverWindow;
    private Snake snake;
    private ObjectInputStream objectInputStream;
    private boolean stop;
    private char dir;

    public ServerReader(ServerWindow serverWindow, Snake snake) {
        this.serverWindow = serverWindow;
        this.snake = snake;
        objectInputStream = null;
        stop = false;
        dir = 'n';
    }

    @Override
    public void run() {
        while(!stop){
            try {
                Thread.sleep(5);
                dir = 'n';
                dir = objectInputStream.readChar();
                serverWindow.setDir(snake, dir);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
