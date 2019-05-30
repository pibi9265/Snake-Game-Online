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
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private GameWindow gameWindow;
    private GameComponent gameComponent;
    private boolean stop;
    private int id;
    private int curPlayer;
    public ArrayList<Snake> snakes;
    public Apple apple;

    public ClientReader(Socket socket, GameWindow gameWindow, GameComponent gameComponent) {
        try {
            this.socket = socket;
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            objectInputStream = null;
        }
        this.gameWindow = gameWindow;
        this.gameComponent = gameComponent;
        stop = false;
        id = -1;
        curPlayer = 0;
        snakes = new ArrayList<Snake>();
        apple = null;
    }

    public void run() {
        while (!stop) {
            try {
                id = objectInputStream.readInt();
                curPlayer = objectInputStream.readInt();
                for (int i = 0; i < curPlayer; i++) {
                    snakes.add((Snake)objectInputStream.readObject());
                }
                apple = (Apple) objectInputStream.readObject();
                if (id >= Board.maxPlayer) {
                    threadStop();
                } else {
                    gameComponent.paintGameComponents(snakes, apple);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                threadStop();
            } finally {
                try {
                    Thread.sleep(Board.sleepTime / 10);
                    curPlayer = 0;
                    snakes.clear();
                    apple = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        id = -1;
        gameWindow.reset();
    }

    public void threadStop(){
        stop = true;
    }

    public int getId(){
        return id;
    }
}
