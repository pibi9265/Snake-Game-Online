/*
package snakegame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import snakegame.element.Apple;
import snakegame.element.Snake;
import snakegame.server.ServerWindow;

public class ServerSender{
    private ObjectOutputStream objectOutputStream;
    
    public ServerSender(Socket socket){
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sending(ArrayList<Snake> snakes, Apple apple){
        try {
            if(objectOutputStream != null){
            	System.out.println("Try to Write Objects");
                objectOutputStream.writeObject(snakes);
                objectOutputStream.writeObject(apple);
                objectOutputStream.reset();
                System.out.println("Writing Objects Complete");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
