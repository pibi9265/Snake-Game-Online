package snakegame;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

import snakegame.Snake;
import snakegame.Apple;
import snakegame.Board;

public class GameComponent extends JComponent{
    private static final long serialVersionUID = 1L;

    private ArrayList<Snake> snakes;
    private Apple apple;

    public boolean start = false;

    public void paintGameComponents(ArrayList<Snake> snake, Apple apple){
        this.snakes = snake;
        this.apple = apple;
        repaint();
    }

    @Override
    public void paint (Graphics g) {
    	if(start)
		{
			g.setColor(Color.black);
			for(int i=0; i<snakes.size(); i++)
			{
				Snake snake = snakes.get(i);
				for(int j = 0;j < snake.maxLength;j++) {
					g.drawRect(snake.body.get(j).x*Board.grid, snake.body.get(j).y*Board.grid, Board.grid , Board.grid);
					g.fillRect(snake.body.get(j).x*Board.grid, snake.body.get(j).y*Board.grid, Board.grid , Board.grid);
				}
			}
			if(apple != null)
			{
				g.setColor(Color.red);
				g.drawRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
				g.fillRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
			}
		}
    }
}
