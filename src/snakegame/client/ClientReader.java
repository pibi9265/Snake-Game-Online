package snakegame.client;

import snakegame.client.PlayerFrame;

public class ClientReader implements Runnable{
    PlayerFrame frame;

    ClientReader(PlayerFrame frame){
        this.frame = frame;
    }
    public void run(){
        frame.repaint();
    }
}
