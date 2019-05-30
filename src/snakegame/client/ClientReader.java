package snakegame.client;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import snakegame.client.GameComponent;
import snakegame.element.Snake;
import snakegame.element.Apple;
import snakegame.element.Board;

public class ClientReader implements Runnable {
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private GameWindow gameWindow;
    private GameComponent gameComponent;
    private boolean stop;
    private int curPlayer;
    private ArrayList<Snake> snakes;
    private Apple apple;
    private char dir;

    public ClientReader(Socket socket, GameWindow gameWindow, GameComponent gameComponent) {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            objectInputStream = null;
        }
        this.gameWindow = gameWindow;
        this.gameComponent = gameComponent;
        stop = false;
        curPlayer = 0;
        snakes = new ArrayList<Snake>();
        apple = null;
    }

    // @SuppressWarnings("unchecked")
    public void run() {
        while (!stop) {
            try {
                //objectOutputStream.writeChar(dir);
                //objectOutputStream.reset();
                if (objectInputStream != null) {
                    curPlayer = objectInputStream.readInt();
                    if (gameWindow.getId() == -1) {
                        gameWindow.setId(curPlayer);
                    }
                    if (gameWindow.getId() > Board.maxPlayer) {
                        threadStop();
                    } else {
                        for (int i = 0; i < curPlayer; i++) {
                            snakes.add((Snake)objectInputStream.readObject());
                        }
                        apple = (Apple) objectInputStream.readObject();

                        gameComponent.paintGameComponents(snakes, apple);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                threadStop();
            } finally {
                try {
                    snakes.clear();
                    apple = null;
                    Thread.sleep(Board.sleepTime / 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameComponent.keyPressed = false;
        }
        try {
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameWindow.reset();
    }

    public void threadStop(){
        stop = true;
    }
    
    public void setDir(char dir)
    {
    	this.dir = dir;
    }
}
