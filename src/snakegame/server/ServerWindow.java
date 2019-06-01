package snakegame.server;

import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.InetAddress;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Random;

import snakegame.element.Snake;
import snakegame.element.SnakeSetDirImpl;
import snakegame.element.SnakeSetDirInterface;
import snakegame.element.Part;
import snakegame.element.Apple;
import snakegame.element.Board;

public class ServerWindow extends Thread {
	public ArrayList<SSLSocket> playerSockets;
	public ArrayList<ObjectOutputStream> objectOutputStreams;
	public ArrayList<ObjectInputStream> objectInputStreams;

	public ArrayList<Snake> snakes;
	public Apple apple;
	public int curPlayer;

	// private ServerAccepter serverAccepter;

	private Random random;
	public Boolean stop;

	private int id;
	private int roomNumber;
	
	private SnakeSetDirInterface ssdi;

	public ServerWindow(int roomNumber) {
		// socket, reader, sender 珥덇린�솕
		playerSockets = new ArrayList<SSLSocket>();
		objectOutputStreams = new ArrayList<ObjectOutputStream>();
		objectInputStreams = new ArrayList<ObjectInputStream>();

		// snake, apple 珥덇린�솕
		snakes = new ArrayList<Snake>();
		apple = new Apple(Board.width / Board.grid - 2, Board.height / Board.grid - 2);
		curPlayer = 0;

		// �굹癒몄� 蹂��닔 珥덇린�솕
		random = new Random();
		stop = true;
		id = -1;
		this.roomNumber = roomNumber;
	}

	synchronized public int addPlayer(SSLSocket player) throws IOException {
		if(curPlayer >= Board.maxPlayer)
		{
			return -1;
		}
		playerSockets.add(player);
		objectOutputStreams.add(new ObjectOutputStream(player.getOutputStream()));
		objectInputStreams.add(new ObjectInputStream(player.getInputStream()));
		curPlayer++;
		snakes.add(new Snake(1, 1));
		return 1;
	}

	synchronized public void delPlayer() throws RemoteException {
		try {
			objectOutputStreams.get(id).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			objectInputStreams.get(id).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			playerSockets.get(id).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		objectOutputStreams.remove(id);
		objectInputStreams.remove(id);
		playerSockets.remove(id);
		snakes.remove(id);
		curPlayer--;
	}
	
	synchronized public boolean isStopped()
	{
		return stop;
	}
	
	public void run() {
		stop = false;
		while (!stop) {
			try {
				synchronized(this) {
					if (curPlayer > 0) {
						for (id = 0; id < curPlayer; id++) {
							objectOutputStreams.get(id).writeInt(roomNumber);
							objectOutputStreams.get(id).writeInt(id);
							objectOutputStreams.get(id).writeObject(snakes);
							objectOutputStreams.get(id).writeObject(apple);
							objectOutputStreams.get(id).reset();
						}
						for (int i = 0; i < curPlayer; i++) {
							move(snakes.get(i));
							shiftDir(snakes.get(i));
						}
						for (int i = 0; i < curPlayer; i++) {
							for (int j = 0; j < curPlayer; j++) {
								if (i != j) {
									collisionHB(snakes.get(i), snakes.get(j));
									collisionHH(snakes.get(i), snakes.get(j));
								}
							}
						}
						collisionHA(snakes, apple);
					}
				}
			} catch (IndexOutOfBoundsException e2) {
				e2.printStackTrace();
			} catch (NullPointerException e3) {
				e3.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					delPlayer();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} finally {
				try {
					Thread.sleep(Board.sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
