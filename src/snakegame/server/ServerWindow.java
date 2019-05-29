package snakegame.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.Random;

//import snakegame.server.ServerReader;
import snakegame.server.ServerSender;
import snakegame.element.Snake;
import snakegame.element.SnakeSetDirImpl;
import snakegame.element.SnakeSetDirInterface;
import snakegame.element.Part;
import snakegame.element.Apple;
import snakegame.server.ServerAccepter;
import snakegame.element.Board;

public class ServerWindow {
	public ServerSocket serverSocket;
	public ArrayList<Socket> playerSockets;
	// public ArrayList<ServerReader> serverReaders;
	public ArrayList<ServerSender> serverSenders;

	private JFrame serverFrame;

	public ArrayList<Snake> snakes;
	public Apple apple;
	public int curPlayer;

	private ServerAccepter serverAccepter;

	private Random random;

	public ServerWindow() {
		try {
			// socket, reader, sender 초기화
			int port = Board.DEFAULT_PORT;
			serverSocket = new ServerSocket(port);
			playerSockets = new ArrayList<Socket>();
			// serverReaders = new ArrayList<ServerReader>();
			serverSenders = new ArrayList<ServerSender>();

			// server 프레임 생성
			serverFrame = new JFrame();
			serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverFrame.setSize(Board.serverFrameWidth, Board.serverFrameHeight);
			serverFrame.setResizable(false);
			// panel 생성
			JPanel panel = new JPanel();
			serverFrame.add(panel);
			// address text area 생성 & panel에 추가
			String addresString = new String(
					"Hello, Snakes! [" + InetAddress.getLocalHost().getHostAddress() + "] [" + port + "]");
			JLabel ipLabel = new JLabel(addresString);
			panel.add(ipLabel);

			// snake, apple 초기화
			snakes = new ArrayList<Snake>();
			apple = new Apple(Board.width / Board.grid - 2, Board.height / Board.grid - 2);
			curPlayer = 0;

			// sever accepter 생성
			serverAccepter = new ServerAccepter(this);

			// 나머지 변수 초기화
			random = new Random();

			// server 프레임 보이기 설정
			serverFrame.setVisible(true);
			serverFrame.requestFocus();
		}
		// 예외처리
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		//////////////////////////////////////////////////////////////////////////////
		try {
		//////////////////////////////////////////////////////////////////////////////

		new Thread(serverAccepter).start();

		SnakeSetDirInterface ssdi = new SnakeSetDirImpl(snakes);
		Naming.rebind("rmi://" + Board.DEFAULT_ADDRESS + ":" + (Board.DEFAULT_PORT+2) + "/" + Board.serverName, ssdi);

		while (true) {
			try {
				if (curPlayer > 0) {
					for (int i = 0; i < curPlayer; i++) {
						//setDir(snakes.get(i), serverReaders.get(i).getDirection());
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
					for (int i = 0; i < curPlayer; i++) {
						serverSenders.get(i).sending();
					}
				}
			} catch (IndexOutOfBoundsException e2) {
				e2.printStackTrace();
			} catch (NullPointerException e3) {
				e3.printStackTrace();
			} finally {
				try {
					Thread.sleep(Board.sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		//////////////////////////////////////////////////////////////////////////////
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//////////////////////////////////////////////////////////////////////////////
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
	
	/*
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
	*/

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
