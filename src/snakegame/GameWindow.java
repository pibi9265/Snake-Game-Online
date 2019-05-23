package snakegame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import snakegame.StartWindow;
import snakegame.GameComponent;
import snakegame.ClientSender;
import snakegame.Snake;
import snakegame.Apple;
import snakegame.Board;

public class GameWindow implements KeyListener{
    private JFrame gameFrame;
    private JFrame startFrame;
	private GameComponent gameComponent;
	private ClientSender clientSender;
    //public Snake snake;
	//public Apple apple;

    public GameWindow(JFrame startFrame, Socket socket){
		this.startFrame = startFrame;
		
		clientSender = new ClientSender(Socket socket);

        gameFrame = new JFrame();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(Board.width+(Board.grid/2), Board.height+(Board.grid*2));
        gameFrame.setResizable(false);
        
        gameComponent = new GameComponent();
        gameFrame.getContentPane().add(gameComponent);
        
		gameFrame.addKeyListener(this);
    }

    JFrame getFrame(){
        return gameFrame;
	}
	
	public void setStart(boolean start){
		gameComponent.start = start;
	}

    public void repaintGameWindow(ArrayList<Snake> snake, Apple apple){
        gameComponent.paintGameComponents(snake, apple);
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			startWindow.clientSender.sending('R');
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			startWindow.clientSender.sending('L');
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			startWindow.clientSender.sending('D');
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP) {
			startWindow.clientSender.sending('U');
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
