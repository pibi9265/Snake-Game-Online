package snakegame.client;

import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import snakegame.client.GameComponent;
import snakegame.element.Snake;
import snakegame.element.Apple;
import snakegame.element.Board;

public class ClientReader implements Runnable {
    private SSLSocket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private GameWindow gameWindow;
    private GameComponent gameComponent;
    private boolean stop;
    private int id;
    private int roomNumber;
    public ArrayList<Snake> snakes;
    public Apple apple;

    public ClientReader(SSLSocket socket, GameWindow gameWindow, GameComponent gameComponent) {
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
        snakes = null;
        apple = null;
    }

    @SuppressWarnings("unchecked")
	public void run() {
        while (!stop) {
            try {
            	roomNumber = objectInputStream.readInt();
                id = objectInputStream.readInt();
                snakes = (ArrayList<Snake>)objectInputStream.readObject();
                apple = (Apple) objectInputStream.readObject();
                gameComponent.paintGameComponents(snakes, apple);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                threadStop();
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
    
    public int getRoomNumber()
    {
    	return roomNumber;
    }
}
