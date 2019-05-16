package snakegame.client;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;

import snakegame.element.snake.*;
import snakegame.element.Apple;
import snakegame.client.GameWindow;

public class ClientReader implements Runnable {
    private ObjectInputStream objectInputStream;
    private GameWindow gameWindow;
    private boolean stop;

    ClientReader(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        objectInputStream = null;
        stop = false;
    }

    public void run() {
        while(!stop){
            try {
                gameWindow.snake = (Snake) objectInputStream.readObject();
                gameWindow.apple = (Apple) objectInputStream.readObject();
                gameWindow.setPaintStatus(true);
                gameWindow.repaint();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSocket(Socket socket){
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void threadStop(){
        gameWindow.setPaintStatus(false);
        stop = true;
    }
}
