package snakegame.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import javax.swing.JFrame;

import snakegame.server.ServerReader;
import snakegame.server.ServerSender;
import snakegame.element.snake.*;
import snakegame.element.Apple;
import snakegame.element.Board;

public class ServerWindow {
	private ServerSocket serverSocket;
	private Socket playerSocket;
	private ServerReader serverReader;
	private ServerSender serverSender;
	private JFrame serverFrame;
	private Snake snake;
	public Apple apple;
	private Random random;

	public ServerWindow() {
		try {
			serverSocket = new ServerSocket(49152);
		} catch (IOException e) {
			e.printStackTrace();
		}
		playerSocket = null;

		serverReader = new ServerReader(this, snake);
		serverSender = new ServerSender(this, snake);

		snake = new Snake(10, 10);
		apple = new Apple(20, 20);
		random = new Random();

		serverFrame = new JFrame();
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setSize(200, 100);
		serverFrame.setResizable(false);

		serverFrame.setVisible(true);
	}

	public void startServer() {
		try {
			playerSocket = serverSocket.accept();
			serverReader.setSocket(playerSocket);
			new Thread(serverReader).start();
			serverSender.setSocket(playerSocket);
			while (true) {
				Thread.sleep(50);
				move(snake);
				shiftDir(snake);
				collisionHA(snake, apple);
				collisionHB(snake, snake);
				serverSender.sending();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public void setDir(Snake snake, char dir){
		if(dir == 'R' && (!snake.body.get(0).left)) {
			snake.body.get(0).dx = 1;
			snake.body.get(0).dy = 0;
			snake.body.get(0).right = true;
			snake.body.get(0).left = false;
			snake.body.get(0).down = false;
			snake.body.get(0).up = false;
		}
		else if(dir == 'L' && (!snake.body.get(0).right)) {
			snake.body.get(0).dx = -1;
			snake.body.get(0).dy = 0;
			snake.body.get(0).right = false;
			snake.body.get(0).left = true;
			snake.body.get(0).down = false;
			snake.body.get(0).up = false;
		}
		else if(dir == 'D' && (!snake.body.get(0).up)) {
			snake.body.get(0).dx = 0;
			snake.body.get(0).dy = 1;
			snake.body.get(0).right = false;
			snake.body.get(0).left = false;
			snake.body.get(0).down = true;
			snake.body.get(0).up = false;
		}
		else if(dir == 'U' && (!snake.body.get(0).down)) {
			snake.body.get(0).dx = 0;
			snake.body.get(0).dy = -1;
			snake.body.get(0).right = false;
			snake.body.get(0).left = false;
			snake.body.get(0).down = false;
			snake.body.get(0).up = true;
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

	private void collisionHA(Snake s, Apple a) {
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
			while(true){
				a.x = random.nextInt(49);
				a.y = random.nextInt(49);
				if(!collisionSA(snake, apple)){
					break;
				}
			}
		}
	}
}
