package snakegame.client;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;

import snakegame.element.Snake;
import snakegame.element.Apple;
import snakegame.element.Board;

public class GameComponent extends JComponent{
    private static final long serialVersionUID = 1L;

    private ArrayList<Snake> snakes;
	private Apple apple;

	public boolean keyPressed;
	
	public GameComponent(){
		snakes = null;
		apple = null;

		keyPressed = false;
	}

    public void paintGameComponents(ArrayList<Snake> snakes, Apple apple){
        this.snakes = snakes;
		this.apple = apple;
		repaint();
    }

    @Override
    public void paint (Graphics g) {
		if(snakes!=null && apple!=null){
			g.setColor(Color.black);
			for(int i=0; i<snakes.size(); i++){
				if(i < Board.maxPlayer){
					for(int j = 0;j < snakes.get(i).maxLength;j++) {
						g.drawRect(snakes.get(i).body.get(j).x*Board.grid, snakes.get(i).body.get(j).y*Board.grid, Board.grid , Board.grid);
						g.fillRect(snakes.get(i).body.get(j).x*Board.grid, snakes.get(i).body.get(j).y*Board.grid, Board.grid , Board.grid);
					}
				}
			}
			g.setColor(Color.red);
			g.drawRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
			g.fillRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
			keyPressed = false;
		}
		snakes = null;
		apple = null;
	}
}
