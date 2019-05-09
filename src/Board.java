import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Iterator;

public class Board extends JComponent implements KeyListener{
	private static final long serialVersionUID = 1L;
	private int weight = 800;
	private int height = 800;
	public int grid = 16;
	
	public ArrayList<Snake> snakes = new ArrayList<Snake>();
	public Apple apple = null;
	public char inputControl = 'R';
	JFrame frame;
	
	public Board()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,weight+(grid/2),height+(grid*2));
		
		frame.getContentPane().add(this);
		frame.addKeyListener(this);
		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
	}
		
	public void paint (Graphics g) {
		if(!snakes.isEmpty())
		{
			g.setColor(Color.black);
			for(int i=0; i<snakes.size(); i++)
			{
				Snake snake = snakes.get(i);
				for(int j = 0;j < snake.maxLength;j++) {
					g.drawRect(snake.body.get(j).x*grid, snake.body.get(j).y*grid, grid , grid);
					g.fillRect(snake.body.get(j).x*grid, snake.body.get(j).y*grid, grid , grid);
				}
			}
		}
		if(apple != null)
		{
			g.setColor(Color.red);
			g.drawRect(apple.x*grid, apple.y*grid, grid , grid);
			g.fillRect(apple.x*grid, apple.y*grid, grid , grid);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			inputControl = 'R';
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			inputControl = 'L';
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			inputControl = 'D';
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP) {
			inputControl = 'U';
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}