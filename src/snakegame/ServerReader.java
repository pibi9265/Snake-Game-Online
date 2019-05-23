package snakegame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.ServerWindow;
import snakegame.Snake;

public class ServerReader implements Runnable {
    private ObjectInputStream objectInputStream;
    private boolean stop;
    private char dir;
 
    public ServerReader(int playerNum) {
        objectInputStream = null;
        stop = false;
        
        if(playerNum == 0)
        {
        	dir = 'R';
        }
        else if(playerNum == 1)
        {
        	dir = 'L';
        }
    }

    @Override
    public void run() {
        while(!stop){
            try {
                dir = objectInputStream.readChar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSocket(Socket socket) {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void threadStop(){
        stop = true;
    }
    
    public char getDirection()
    {
    	return dir;
    }
}
