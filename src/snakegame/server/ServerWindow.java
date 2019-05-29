package snakegame.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.net.InetAddress;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import snakegame.element.Snake;
import snakegame.element.Part;
import snakegame.element.Apple;
import snakegame.element.Board;

public class ServerWindow extends Thread{
	public ArrayList<Socket> playerSockets;
	public ArrayList<ObjectOutputStream> objectOutputStreams;
	public ArrayList<ObjectInputStream> objectInputStreams;

	public ArrayList<Snake> snakes;
	public Apple apple;
	public int curPlayer;

	//private ServerAccepter serverAccepter;

	private Random random;
	public Boolean stopped;
	
	public ServerWindow() {
		// socket, reader, sender 초기화
		playerSockets = new ArrayList<Socket>();
		objectOutputStreams = new ArrayList<ObjectOutputStream>();
		objectInputStreams = new ArrayList<ObjectInputStream>();
		
		// snake, apple 초기화
		snakes = new ArrayList<Snake>();
		apple = new Apple(Board.width/Board.grid - 2, Board.height/Board.grid - 2);
		curPlayer = 0;
		
		// 나머지 변수 초기화
		random = new Random();
		
		stopped = true;
	}
	
	synchronized public int addPlayer(Socket player) throws IOException
	{
		if(curPlayer >= Board.maxPlayer)
			return -1;
		
		playerSockets.add(player);
		objectOutputStreams.add(new ObjectOutputStream(player.getOutputStream()));
		objectInputStreams.add(new ObjectInputStream(player.getInputStream()));
        curPlayer++;
        snakes.add(new Snake(1, 1));
        
        return 1;
	}
	
	public Boolean isStopped()
	{
		return stopped;
	}
	
	public void run() {
		stopped = false;
		while (!stopped) {
			try {
				synchronized(this)
				{
					if (curPlayer > 0) {
						
						for (int i = 0; i < curPlayer; i++) {
							objectOutputStreams.get(i).writeObject(snakes);
							objectOutputStreams.get(i).writeObject(apple);
							objectOutputStreams.get(i).reset();
						}
						
						for (int i = 0; i < curPlayer; i++) {
							char dirInput = objectInputStreams.get(i).readChar();
							setDir(snakes.get(i), dirInput);
							move(snakes.get(i));
							shiftDir(snakes.get(i));
						}
						for (int i = 0; i < curPlayer; i++) {
							for (int j = 0; j < curPlayer; j++) {
								collisionHB(snakes.get(i), snakes.get(j));
								if (i != j) {
									collisionHH(snakes.get(i), snakes.get(j));
								}
							}
						}
						collisionHA(snakes, apple);
					}
					else
					{
						stopped = true;
						playerSockets.clear();
					}
				}
				
				Thread.sleep(Board.sleepTime);
			} catch (IndexOutOfBoundsException e2) {
				e2.printStackTrace();
			} catch (NullPointerException e3) {
				e3.printStackTrace();
			} catch (InterruptedException e) {
					e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				/* 레벨업 기능
				if(s.maxLength > 10) {
					s.updateLevel();
				}
				*/
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
