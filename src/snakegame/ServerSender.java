package snakegame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import snakegame.ServerWindow;
import snakegame.Snake;

public class ServerSender{
    private ArrayList<Snake> snake;
    private ObjectOutputStream objectOutputStream;
    private Apple apple;
    
    public ServerSender(ArrayList<Snake> snake, Apple apple){
        this.snake = snake;
        this.apple = apple;
        objectOutputStream = null;
    }

    public void sending(){
        try {
            if(objectOutputStream != null){
                objectOutputStream.writeObject(snake);
                objectOutputStream.writeObject(apple);
                objectOutputStream.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
