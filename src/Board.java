import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.Random;

public class Board extends JComponent implements KeyListener{
	private int weight = 800;
	private int height = 800;
	private Snake snake = new Snake(10, 10);
	private Apple apple = new Apple(20, 20);
	private Random random = new Random();
	public int grid = 16;
	
	public void init () {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0,weight,height);
		
		frame.getContentPane().add(this);
		frame.addKeyListener(this);
		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
		
		try {
			while(true) {
				Thread.sleep(50);
				move();
				repaint();
				shiftDir();
				collisionHB(snake, snake);
				collisionSA(snake, apple);
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void paint (Graphics g) {
		for(int i = 0;i < snake.maxLength;i++) {
			g.drawRect(snake.body.get(i).x*grid, snake.body.get(i).y*grid, grid , grid);
		}
		g.drawRect(apple.x*grid, apple.y*grid, grid , grid);
	}
	
	private void shiftDir() {
		for(int i = (snake.maxLength-1);i > 0;i--) {
			if(snake.body.get(i).dx != snake.body.get(i-1).dx) {
				snake.body.get(i).dx = snake.body.get(i-1).dx;
			}
			if(snake.body.get(i).dy != snake.body.get(i-1).dy) {
				snake.body.get(i).dy = snake.body.get(i-1).dy;
			}
		}
	}
	
	private void move() {
		for(int i = 0;i < snake.maxLength;i++) {
			snake.body.get(i).x += snake.body.get(i).dx;
			if(snake.body.get(i).x > (weight/grid)) {
				snake.body.get(i).x = 0;
			}
			else if(snake.body.get(i).x < 0) {
				snake.body.get(i).x = (weight/grid);
			}
			snake.body.get(i).y += snake.body.get(i).dy;
			if(snake.body.get(i).y > (height/grid)) {
				snake.body.get(i).y = 0;
			}
			else if(snake.body.get(i).y < 0) {
				snake.body.get(i).y = (height/grid);
			}
		}
	}
	
	private void collisionHB(Snake h, Snake b) {
		if(b.maxLength!=1) {
			for(int i = 1;i < b.maxLength;i++) {
				if((h.body.get(0).x==b.body.get(i).x)&&(h.body.get(0).y==b.body.get(i).y)) {
					while(b.maxLength!=i) {
						b.body.remove(b.maxLength-1);
						b.maxLength--;
					}
					return;
				}
			}
		}
	}
	
	private void collisionHH(Snake h1, Snake h2) {
		if((h1.body.get(0).x==h2.body.get(0).x)&&(h1.body.get(0).y==h2.body.get(0).y)) {
			while(h1.maxLength!=1) {
				h1.body.remove(h1.maxLength-1);
				h1.maxLength--;
			}
			while(h2.maxLength!=1) {
				h2.body.remove(h2.maxLength-1);
				h2.maxLength--;
			}
		}
	}
	
	private void collisionSA(Snake s, Apple a) {
		if((s.body.get(0).x==a.x)&&(s.body.get(0).y==a.y)) {
			s.body.add(new Part());
			s.maxLength++;
			s.body.get(s.maxLength-1).x = s.body.get(s.maxLength-2).x;
			s.body.get(s.maxLength-1).y = s.body.get(s.maxLength-2).y;
			s.body.get(s.maxLength-1).dx = s.body.get(s.maxLength-2).dx;
			s.body.get(s.maxLength-1).dy = s.body.get(s.maxLength-2).dy;
			s.body.get(s.maxLength-1).right = s.body.get(s.maxLength-2).right;
			s.body.get(s.maxLength-1).left = s.body.get(s.maxLength-2).left;
			s.body.get(s.maxLength-1).down = s.body.get(s.maxLength-2).down;
			s.body.get(s.maxLength-1).up = s.body.get(s.maxLength-2).up;
			s.body.get(s.maxLength-1).x -= s.body.get(s.maxLength-1).dx;
			s.body.get(s.maxLength-1).y -= s.body.get(s.maxLength-1).dy;
			a.x = random.nextInt(50);
			a.y = random.nextInt(50);
		}
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
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
	}
}