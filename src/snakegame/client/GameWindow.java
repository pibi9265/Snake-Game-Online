package snakegame.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import snakegame.client.StartWindow;
import snakegame.client.GameComponent;
import snakegame.element.snake.*;
import snakegame.element.Apple;
import snakegame.element.Board;

public class GameWindow implements KeyListener{
    private JFrame gameFrame;
    private StartWindow StartWindow;
    private GameComponent gameComponent;
    public Snake snake;
    public Apple apple;

    public GameWindow(StartWindow StartWindow){
        this.StartWindow = StartWindow;

        GameComponent gameComponent = new GameComponent();

        gameFrame = new JFrame();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(Board.width, Board.height);
        gameFrame.setResizable(false);
        
        gameComponent = new GameComponent();
        gameComponent.init(snake, apple);
        gameFrame.getContentPane().add(gameComponent);
        
		gameFrame.addKeyListener(this);
    }

    JFrame getFrame(){
        return gameFrame;
	}
	
	public void setPaintStatus(boolean paintStatus){
		gameComponent.paintStatus = paintStatus;
	}

    public void repaint(){
        gameComponent.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //sender
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //sender
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //sender
    }

    /*
    	public void keyPressed(KeyEvent e) {
		if(!snake.keysPressed)
		{
			if((e.getKeyCode()==KeyEvent.VK_RIGHT)&&(!snake.body.get(0).left)) {
			snake.body.get(0).dx = 1;
			snake.body.get(0).dy = 0;
			snake.body.get(0).right = true;
			snake.body.get(0).left = false;
			snake.body.get(0).down = false;
			snake.body.get(0).up = false;
			}
			else if((e.getKeyCode()==KeyEvent.VK_LEFT)&&(!snake.body.get(0).right)) {
				snake.body.get(0).dx = -1;
				snake.body.get(0).dy = 0;
				snake.body.get(0).right = false;
				snake.body.get(0).left = true;
				snake.body.get(0).down = false;
				snake.body.get(0).up = false;
			}
			else if((e.getKeyCode()==KeyEvent.VK_DOWN)&&(!snake.body.get(0).up)) {
				snake.body.get(0).dx = 0;
				snake.body.get(0).dy = 1;
				snake.body.get(0).right = false;
				snake.body.get(0).left = false;
				snake.body.get(0).down = true;
				snake.body.get(0).up = false;
			}
			else if((e.getKeyCode()==KeyEvent.VK_UP)&&(!snake.body.get(0).down)) {
				snake.body.get(0).dx = 0;
				snake.body.get(0).dy = -1;
				snake.body.get(0).right = false;
				snake.body.get(0).left = false;
				snake.body.get(0).down = false;
				snake.body.get(0).up = true;
			}
			snake.keysPressed = true;
		}
    */
}
