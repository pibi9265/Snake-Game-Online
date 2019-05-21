package snakegame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

import snakegame.ServerReader;
import snakegame.ServerSender;
import snakegame.Snake;
import snakegame.Part;
import snakegame.Apple;
import snakegame.Board;

public class ServerWindow implements Runnable{
	private ServerSocket serverSocket;
	private ArrayList<Socket> playerSockets;
	private ArrayList<ServerReader> serverReaders;
	private ArrayList<ServerSender> serverSenders;
	private JFrame serverFrame;
	private ArrayList<Snake> snake;
	public Apple apple;
	private Random random;
	private boolean stop;
	private int playerCount;
	
	public ServerWindow() {
		random = new Random();
		snake = new ArrayList<Snake>();
		//snake = new Snake(10, 10);
		apple = new Apple(20, 20);

		//serverReader = new ServerReader(this, snake);
		serverReaders = new ArrayList<ServerReader>();
		//serverSender = new ServerSender(this, snake);
		serverSenders = new ArrayList<ServerSender>();
		
		serverSocket = null;
		//playerSocket = null;
		playerSockets = new ArrayList<Socket>();
		
		serverFrame = new JFrame();
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setSize(400, 200);
		serverFrame.setResizable(false);
		serverFrame.setVisible(true);

		stop = false;
		playerCount = 0;
		new Thread(this).start();
	}

	public void run(){
		try {
			serverSocket = new ServerSocket(49152);
			while(playerCount != 2) 
			{
				playerSockets.add(serverSocket.accept());
				snake.add(new Snake(playerCount));
				serverSenders.add(new ServerSender(snake, apple));
				serverSenders.get(playerCount).setSocket(playerSockets.get(playerCount));
				serverReaders.add(new ServerReader());
				serverReaders.get(playerCount).setSocket(playerSockets.get(playerCount));
				playerCount++;
			}
			for(int i=0; i<playerCount; i++)
			{
				new Thread(serverReaders.get(i)).start();
			}
			
			while (!stop) {
				for(int i=0; i<playerCount; i++)
		    	{
					setDir(snake.get(i), serverReaders.get(i).getDirection());
					move(snake.get(i));
					shiftDir(snake.get(i));
		    	}
				
				for(int i=0; i<playerCount; i++)
				{
					for(int j=0; j<playerCount; j++)
		        	{
	        			collisionHB(snake.get(i), snake.get(j));
	        			if(i != j)
	        			{
	        				collisionHH(snake.get(i), snake.get(j));
	        			}
		        	}	    
				}
				collisionHA(snake, apple);
				for(int i=0; i<playerCount; i++)
				{
					serverSenders.get(i).sending();
				}
				Thread.sleep(5);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void threadStop(){
        stop = true;
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
		for(int i=0; i<snakes.size(); i++)
		{
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
				
				while(true){
					a.x = random.nextInt(49);
					a.y = random.nextInt(49);
					
					for(int j=0; j<snakes.size(); j++)
					{
						if(collisionSA(snakes.get(j), a)){
							continue;
						}
					}
					break;
				}
				/*
				if(s.maxLength > 10)
				{
					s.updateLevel();
				}
				*/
			}
		}
	}
}
