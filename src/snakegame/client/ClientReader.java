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
    
    public ClientReader(Socket socket,GameWindow gameWindow, GameComponent gameComponent) {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        	objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            objectInputStream = null;
        }
        this.gameWindow = gameWindow;
        this.gameComponent = gameComponent;
        stop = false;
        curPlayer = -1;
        snakes = null;
        apple = null;
    }

    @SuppressWarnings("unchecked")
	public void run() {
        while (!stop) {
            try {
            	objectOutputStream.writeChar(dir);
                objectOutputStream.reset();
                
                if (objectInputStream != null) {
                	curPlayer = objectInputStream.readInt();
                    snakes = (ArrayList<Snake>) objectInputStream.readObject();
                    apple = (Apple) objectInputStream.readObject();
                    gameComponent.paintGameComponents(snakes, apple);
                   }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                threadStop();
            } finally {
            }
            gameComponent.keyPressed = false;
        }
        try {
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //gameWindow.reset();
    }

    public void threadStop(){
        stop = true;
    }
    
    public void setDir(char dir)
    {
    	this.dir = dir;
    }
}
