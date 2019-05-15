package snakegame.client;

import java.net.Socket;

public class ClientReader implements Runnable{
    Socket socket;

    ClientReader(){
        socket = null;
    }

    public void run(){}

    public void setSocket(Socket socket){
        this.socket = socket;
    }
}
