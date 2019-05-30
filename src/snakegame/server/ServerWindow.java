package snakegame.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.InetAddress;
import java.net.MalformedURLException;
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
import snakegame.element.SnakeSetDirImpl;
import snakegame.element.SnakeSetDirInterface;
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
	private int i;
	
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
		
		if(curPlayer == 0)
		{
			apple = new Apple(Board.width/Board.grid - 2, Board.height/Board.grid - 2);
		}
		
		playerSockets.add(player);
		objectOutputStreams.add(new ObjectOutputStream(player.getOutputStream()));
		objectInputStreams.add(new ObjectInputStream(player.getInputStream()));
        curPlayer++;
        snakes.add(new Snake(1, 1));
        
        return 1;
	}
	
	synchronized public void delPlayer(int player)
	{
		try {
			playerSockets.get(i).close();
			playerSockets.remove(i);
			objectOutputStreams.remove(i);
			objectInputStreams.remove(i);
			snakes.remove(i);
			curPlayer--;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean isStopped()
	{
		return stopped;
	}
	
	public void run() {
		stopped = false;
		try {
			SnakeSetDirInterface ssdi;
			ssdi = new SnakeSetDirImpl(snakes);
			Naming.rebind("rmi://" + Board.DEFAULT_ADDRESS + ":" + (Board.DEFAULT_PORT+2) + "/" + Board.serverName, ssdi);
		} catch (RemoteException | MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (!stopped) {
			try {
				synchronized(this)
				{
					if (curPlayer > 0) {
						
						for (i = 0; i < curPlayer; i++) {
							objectOutputStreams.get(i).writeObject(snakes);
							objectOutputStreams.get(i).writeObject(apple);
							objectOutputStreams.get(i).reset();
						}
						
						for (i = 0; i < curPlayer; i++) {
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
				delPlayer(i);
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
