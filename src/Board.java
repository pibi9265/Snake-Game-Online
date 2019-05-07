import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.w3c.dom.events.Event;

import java.util.Random;

public class Board extends JComponent{
	private int weight = 800;
	private int height = 800;
	public Snake[] snakes = null;
	public Apple apple;
	public int grid = 16;
	
	public void init () {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,weight+(grid/2),height+(grid*2));
		
		frame.getContentPane().add(this);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
	}
	
	public void paint (Graphics g) {
		if(!(snakes == null))
		{
			g.setColor(Color.black);
			for(int i=0; i<snakes.length; i++)
			{
				Snake snake = snakes[i];
				for(int j = 0;j < snake.maxLength;j++) {
					g.drawRect(snake.body.get(i).x*grid, snake.body.get(i).y*grid, grid , grid);
					g.fillRect(snake.body.get(i).x*grid, snake.body.get(i).y*grid, grid , grid);
				}
			}
			g.setColor(Color.red);
			g.drawRect(apple.x*grid, apple.y*grid, grid , grid);
			g.fillRect(apple.x*grid, apple.y*grid, grid , grid);
		}
	}
}