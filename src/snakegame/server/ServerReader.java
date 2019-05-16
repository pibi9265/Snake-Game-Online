package snakegame.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.server.ServerWindow;
import snakegame.element.snake.*;

public class ServerReader implements Runnable {
    private ServerWindow serverWindow;
    private Snake snake;
    //private Socket socket;
    BufferedInputStream bufferedInputStream;
    private ObjectInputStream objectInputStream;
    private boolean stop;
    private char dir;

    public ServerReader(ServerWindow serverWindow, Snake snake) {
        this.serverWindow = serverWindow;
        this.snake = snake;
        //socket = null;
        bufferedInputStream = null;
        objectInputStream = null;
        stop = false;
        dir = 'n';
    }

    @Override
    public void run() {
        while(!stop){
            try {
                //objectInputStream = new ObjectInputStream(socket.getInputStream());
                dir = 'n';
                dir = objectInputStream.readChar();
                serverWindow.setDir(snake, dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSocket(Socket socket) {
        //this.socket = socket;
        try {
            bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            objectInputStream = new ObjectInputStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void threadStop(){
        stop = true;
    }
}
