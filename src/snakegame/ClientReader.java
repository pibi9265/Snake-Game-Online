package snakegame;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;

import snakegame.Snake;
import snakegame.Apple;
import snakegame.GameWindow;

public class ClientReader implements Runnable {
    private ObjectInputStream objectInputStream;
    private GameWindow gameWindow;
    private boolean stop;
    ArrayList<Snake> snake;
    Apple apple;
    
    public ClientReader(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        objectInputStream = null;
        stop = false;
        snake = null;
        apple = null;
        
    }

    public void run() {
        while(!stop){
            try {
                snake = (ArrayList<Snake>)objectInputStream.readObject();
                apple = (Apple)objectInputStream.readObject();
                gameWindow.setStart(true);
                gameWindow.repaintGameWindow(snake, apple);
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
        gameWindow.setStart(false);
        stop = true;
    }
}
