package snakegame;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;

import snakegame.Snake;
import snakegame.Apple;
import snakegame.GameWindow;

public class ClientReader implements Runnable {
    private ObjectInputStream objectInputStream;
    private GameWindow gameWindow;
    private boolean stop;

    public ClientReader(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        objectInputStream = null;
        stop = false;
    }

    public void run() {
        while(!stop){
            try {
                Thread.sleep(5);
                Snake snake = (Snake)objectInputStream.readObject();
                Apple apple = (Apple)objectInputStream.readObject();
                gameWindow.setStart(true);
                gameWindow.repaintGameWindow(snake, apple);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
        gameWindow.setStart(false);
        stop = true;
    }
}