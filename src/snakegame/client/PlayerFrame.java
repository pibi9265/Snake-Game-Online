package snakegame.client;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;

import snakegame.element.snake.*;
import snakegame.element.Apple;
import snakegame.element.Board;
import snakegame.client.ClientSender;
import snakegame.client.ClientReader;

public class PlayerFrame extends JComponent implements KeyListener{
	private static final long serialVersionUID = 1L;

	private PlayerFrame thisFrame = this;

	private String server = "127.0.0.1";
	private int port = 49152;

    private Snake snake;
	private Apple apple;

	public void init () {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,80);
		frame.setResizable(false);

		///////////////////////////////////////////////////////////////
		JPanel panel = new JPanel();
		frame.add(panel);

		JButton button = new JButton("Start");
		panel.add(button);

		JTextArea ipTextArea = new JTextArea("127.0.0.1");
		ipTextArea.setColumns(10);
		ipTextArea.setRows(1);
		panel.add(ipTextArea);

		JTextArea portTextArea = new JTextArea("49152");
		portTextArea.setColumns(6);
		portTextArea.setRows(1);
		panel.add(portTextArea);

		button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				thisFrame.server = portTextArea.getText();
				thisFrame.port = Integer.parseInt(portTextArea.getText());
				frame.setVisible(false);
				new Thread(new ClientReader(thisFrame)).start();
				startGame();
            }
		});
		//////////////////////////////////////////////////////////////////

		frame.setVisible(true);
		frame.requestFocus();
	}

	public void startGame(){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,Board.weight+(Board.grid/2),Board.height+(Board.grid*2));
		
		frame.getContentPane().add(this);
		frame.addKeyListener(this);
		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
	}

	public void paint (Graphics g) {
		g.setColor(Color.black);
		for(int i = 0;i < snake.maxLength;i++) {
			g.drawRect(snake.body.get(i).x*Board.grid, snake.body.get(i).y*Board.grid, Board.grid , Board.grid);
			g.fillRect(snake.body.get(i).x*Board.grid, snake.body.get(i).y*Board.grid, Board.grid , Board.grid);
		}
		
		g.setColor(Color.red);
		g.drawRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
		g.fillRect(apple.x*Board.grid, apple.y*Board.grid, Board.grid , Board.grid);
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
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
	}
}
