package snakegame.server;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Random;

import snakegame.element.Snake;
import snakegame.element.SnakeControllerImpl;
import snakegame.element.Part;
import snakegame.element.Apple;
import snakegame.element.Board;

import snakegame.rmisslsocketfactory.RMISSLServerSocketFactory;
import snakegame.rmisslsocketfactory.RMISSLClientSocketFactory;

public class ServerWindow extends Thread {
	private JFrame serverFrame;
	private JLabel label;

	public ArrayList<Snake> snakes;
	public Apple apple;

	private Random random;

	public ServerWindow() {
		try {
			// server 프레임 생성
			serverFrame = new JFrame();
			serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverFrame.setSize(Board.serverFrameWidth, Board.serverFrameHeight);
			serverFrame.setResizable(false);
			// panel 생성
			JPanel panel = new JPanel();
			serverFrame.add(panel);
			// address text area 생성 & panel에 추가
			String addresString = new String("Hello, Snakes! [" + InetAddress.getLocalHost().getHostAddress() + "]");
			label = new JLabel(addresString);
			panel.add(label);

			// snake, apple 초기화
			snakes = new ArrayList<Snake>();
			apple = new Apple(Board.width / Board.grid - 2, Board.height / Board.grid - 2);

			// 나머지 변수 초기화
			random = new Random();

			// server 프레임 보이기 설정 (ServerDo로 옮김)
			//serverFrame.setVisible(true);
			//serverFrame.requestFocus();
		}
		// 예외처리
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
            Registry registry = LocateRegistry.createRegistry(Board.DEFAULT_PORT, new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
            SnakeControllerImpl snakeController = new SnakeControllerImpl(snakes, apple);
			registry.bind(Board.snakeControllerName, snakeController);
			while (true) {
				try{
					if (snakeController.getSize() > 0) {
						for (int i = 0; i < snakeController.getSize(); i++) {
							move(snakes.get(i));
							shiftDir(snakes.get(i));
						}
						for (int i = 0; i < snakeController.getSize(); i++) {
							for (int j = 0; j < snakeController.getSize(); j++) {
								if (i != j) {
									collisionHH(snakes.get(i), snakes.get(j));
								}		
								collisionHB(snakes.get(i), snakes.get(j));
							}
						}
						collisionHA(snakes, apple);
					}
					Thread.sleep(Board.sleepTime);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			label.setText("RMI Error");
			return;
        }
	}

	public JFrame getFrame() {
		return serverFrame;
	}

  private void move(Snake snake) {
		for(int i = 0;i < snake.maxLength;i++) {
			snake.body.get(i).x += snake.body.get(i).dx;
			if(snake.body.get(i).x > (Board.width/Board.grid) - 1) {
				snake.body.get(i).x = 0;
			}
			else if(snake.body.get(i).x < 0) {
				snake.body.get(i).x = (Board.width/Board.grid) - 1;
			}
			snake.body.get(i).y += snake.body.get(i).dy;
			if(snake.body.get(i).y > (Board.height/Board.grid) - 1) {
				snake.body.get(i).y = 0;
			}
			else if(snake.body.get(i).y < 0) {
				snake.body.get(i).y = (Board.height/Board.grid) - 1;
			}
		}
	}

  private void shiftDir(Snake snake) {
		for(int i = (snake.maxLength-1);i > 0;i--) {
			if(snake.body.get(i).dx != snake.body.get(i-1).dx) {
				snake.body.get(i).dx = snake.body.get(i-1).dx;
			}
			if(snake.body.get(i).dy != snake.body.get(i-1).dy) {
				snake.body.get(i).dy = snake.body.get(i-1).dy;
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
	
	private boolean collisionSA(Snake s, Apple a){
		for(int i = 0;i < s.maxLength;i++) {
			if((a.x==s.body.get(i).x)&&(a.y==s.body.get(i).y)) {
				return true;
			}
		}
		return false;
	}

	private void collisionHA(ArrayList<Snake> snakes, Apple a) {
		boolean check = false;

		for(int i=0; i<snakes.size(); i++) {
			Snake s = snakes.get(i);
			
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
				check = true;
			}
		}

		while(check){
			a.x = random.nextInt(49);
			a.y = random.nextInt(49);
			check = false;
			
			for(int i=0; i<snakes.size(); i++) {
				if(collisionSA(snakes.get(i), a)){
					check = true;
				}
			}
		}
	}
}
