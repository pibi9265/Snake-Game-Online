package snakegame;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
//import java.io.BufferedInputStream;

import snakegame.Snake;
import snakegame.Apple;
import snakegame.GameWindow;

public class ClientReader implements Runnable {
    //private Socket socket;
    //BufferedInputStream bufferedInputStream;
    private ObjectInputStream objectInputStream;
    private GameWindow gameWindow;
    private boolean stop;

    public ClientReader(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        //socket = null;
        //bufferedInputStream = null;
        objectInputStream = null;
        stop = false;
    }

    public void run() {
        while(!stop){
            try {
                //objectInputStream = new ObjectInputStream(socket.getInputStream());
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
        //this.socket = socket;
        try {
            //bufferedInputStream = new BufferedInputStream(socket.getInputStream());
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
