package snakegame.client;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;

import snakegame.element.snake.*;
import snakegame.element.Apple;
import snakegame.client.GameWindow;

public class ClientReader implements Runnable {
    private Socket socket;
    GameWindow gameWindow;
    boolean stop;

    ClientReader(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        socket = null;
        stop = false;
    }

    public void run() {
        ObjectInputStream objectInputStream;
        while(!stop){
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                gameWindow.snake = (Snake) objectInputStream.readObject();
                gameWindow.apple = (Apple) objectInputStream.readObject();
                gameWindow.setPaintStatus(true);
                gameWindow.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public void threadStop(){
        gameWindow.setPaintStatus(false);
        stop = true;
    }
}
