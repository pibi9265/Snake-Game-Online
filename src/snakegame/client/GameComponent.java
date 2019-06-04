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
	
	private int id;

    private ArrayList<Snake> snakes;
	private Apple apple;

	public boolean keyPressed;
	
	public GameComponent(){
		id = -1;

		snakes = null;
		apple = null;

		keyPressed = false;
	}

	public void reset(){
		id = -1;

		snakes = null;
		apple = null;

		keyPressed = false;
	}

    public void paintGameComponents(int id, ArrayList<Snake> snakes, Apple apple){
		this.id = id;
        this.snakes = snakes;
		this.apple = apple;
		repaint();
    }

    @Override
    public void paint (Graphics g) {
		if(id != -1){
			for(int i=0; i<snakes.size(); i++){
				g.setColor(new Color(snakes.get(i).r, snakes.get(i).g, snakes.get(i).b));
				for(int j = 0;j < snakes.get(i).maxLength;j++) {
					g.drawRect(snakes.get(i).body.get(j).x*Board.grid, snakes.get(i).body.get(j).y*Board.grid, Board.grid , Board.grid);
					g.fillRect(snakes.get(i).body.get(j).x*Board.grid, snakes.get(i).body.get(j).y*Board.grid, Board.grid , Board.grid);
				}
			}

			g.setColor(Color.black);
			g.drawRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
			g.fillRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);

			keyPressed = false;
		}

		id = -1;

		snakes = null;
		apple = null;
	}
}
