package snakegame;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;

import snakegame.Snake;
import snakegame.Apple;
import snakegame.Board;

public class GameComponent extends JComponent{
    private static final long serialVersionUID = 1L;

    private Snake snake;
    private Apple apple;

    public boolean paintStatus = false;

    public void init(Snake snake, Apple apple){
        this.snake = snake;
        this.apple = apple;
    }

    @Override
    public void paint (Graphics g) {
        if(paintStatus){
	    	g.setColor(Color.black);
	    	for(int i = 0;i < snake.maxLength;i++) {
	    		g.drawRect(snake.body.get(i).x*Board.grid, snake.body.get(i).y*Board.grid, Board.grid , Board.grid);
	    		g.fillRect(snake.body.get(i).x*Board.grid, snake.body.get(i).y*Board.grid, Board.grid , Board.grid);
            }

	    	g.setColor(Color.red);
	    	g.drawRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
	    	g.fillRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
        }
    }
}
