package snakegame.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import snakegame.element.Snake;
import snakegame.element.Board;

public class ServerReader implements Runnable {
    private ObjectInputStream objectInputStream;
    private boolean stop;
    private char dir;
    private Socket playerSocket;
    private Snake snake;
    private ServerSender serverSender;
    private ServerWindow serverWindow;

    public ServerReader(Socket playerSocket, Snake snake, ServerSender serverSender, ServerWindow serverWindow) {
        try {
            objectInputStream = new ObjectInputStream(playerSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stop = false;
        dir = 'N';
        this.playerSocket = playerSocket;
        this.snake = snake;
        this.serverSender = serverSender;
        this.serverWindow = serverWindow;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                dir = objectInputStream.readChar();
            } catch (IOException e1) {
                e1.printStackTrace();

                serverWindow.snakes.remove(snake);
                serverWindow.playerSockets.remove(playerSocket);
                serverWindow.serverReaders.remove(this);
                serverWindow.serverSenders.remove(serverSender);
                serverWindow.curPlayer--;

                threadStop();
                try {
                    objectInputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } finally {
                try {
                    Thread.sleep(Board.sleepTime/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void threadStop(){
        stop = true;
    }

    public char getDirection(){
    	return dir;
    }
}
