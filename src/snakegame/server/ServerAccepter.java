package snakegame.server;

import java.io.IOException;
import java.io.ObjectOutputStream;

import snakegame.server.ServerWindow;
//import snakegame.server.ServerReader;
import snakegame.server.ServerSender;
import snakegame.element.Snake;
import snakegame.element.Board;

public class ServerAccepter implements Runnable {
    private ServerWindow serverWindow;

    private boolean stop;

    public ServerAccepter(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;
        stop = false;
    }

    @Override
    public void run() {
        while(!stop){
            try {
                serverWindow.playerSockets.add(serverWindow.serverSocket.accept());
                serverWindow.curPlayer++;
                if(serverWindow.curPlayer <= Board.maxPlayer){
                    serverWindow.snakes.add(new Snake(1, 1));

                    serverWindow.serverSenders.add(new ServerSender(serverWindow.playerSockets.get(serverWindow.curPlayer-1), serverWindow, serverWindow.snakes.get(serverWindow.curPlayer-1)));

                    //serverWindow.serverReaders.add(new ServerReader(serverWindow.playerSockets.get(serverWindow.curPlayer-1), serverWindow.snakes.get(serverWindow.curPlayer-1), serverWindow.serverSenders.get(serverWindow.curPlayer-1), serverWindow));
                    //new Thread(serverWindow.serverReaders.get(serverWindow.curPlayer-1)).start();
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverWindow.playerSockets.get(serverWindow.curPlayer-1).getOutputStream());
                    serverWindow.playerSockets.remove(serverWindow.curPlayer-1);
                    objectOutputStream.writeInt(serverWindow.curPlayer);
                    serverWindow.curPlayer--;
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void threadStop(){
        stop = true;
    }
}
