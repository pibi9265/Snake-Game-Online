import java.net.*;
import java.util.Random;

import javax.swing.JFrame;

import java.io.*;

class GameServerDo {
	public static void main(String args[]) {
		int serverPort = 5001;
		
		GameServer server = new GameServer(serverPort);
		new Thread(server).start();
	}
}

class GameServer implements Runnable {
	protected int serverPort;
	protected ServerSocket serverSocket;
	Socket playerSocket[] = new Socket[4];
	int playerCount = 0;
	boolean gameStart = false;
	
	public GameServer(int port)
	{
		this.serverPort = port;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch(IOException e)
		{
			throw new RuntimeException("Cannot open port "+serverPort, e);
		}
		
		while(true) {
			Socket clientSocket = null;
			
			try {
				clientSocket = serverSocket.accept();
				playerSocket[playerCount] = clientSocket;
				playerCount++;
				
				if(playerCount == 1)
				{
					gameStart = true;
				}
				if(gameStart)
				{
					break;
				}
			} catch (IOException e) {
				throw new RuntimeException(
					"Error accepting client connection", e);
			}
		}
		
		try {
			new Thread(new PlayerManager(playerSocket, playerCount)).start();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class PlayerManager implements Runnable {
	Socket[] clientSocket;
	Snake[] snake;
	char dirInput;
	int playerCount;
	
	//보드 컨트롤 정보들
	private int weight = 800;
	private int height = 800;
	public int grid = 16;
	private Random random = new Random();
	private Apple apple = new Apple(20, 20);
	
	PlayerManager(Socket[] socket, int playerCount) throws IOException, ClassNotFoundException
	{
		clientSocket = socket;
		this.playerCount = playerCount;
		snake = new Snake[playerCount];
	}
	
	public void run()
	{
		while(true)
		{
			for(int i=0; i<playerCount; i++)
			{
				try {
			        ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket[i].getInputStream());
			        snake[i] = (Snake)objectInputStream.readObject();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for(int i=0; i<playerCount; i++)
	    	{
				try {
					Reader charInputStream = new InputStreamReader(clientSocket[i].getInputStream());
					dirInput = (char) charInputStream.read();
					
		    		setDirection(snake[i], dirInput);
		        	move(snake[i]);
		        	shiftDir(snake[i]);
		        	for(int j=0; j<playerCount; j++)
		        	{
	        			collisionHB(snake[i], snake[j]);
	        			if(i != j)
	        			{
	        				collisionHH(snake[i], snake[j]);
	        			}
		        	}	        	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
			
			collisionHA(snake, apple);
			
			for(int i=0; i<playerCount; i++)
	    	{
				try {
					OutputStream outputStream = clientSocket[i].getOutputStream();
					outputStream.write(i);
					
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket[i].getOutputStream());
					objectOutputStream.writeObject(snake);
					objectOutputStream.writeObject(apple);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		}
	}
	
	private void setDirection(Snake snake, char direction)
	{
		if(!snake.keysPressed)
		{
			if(direction == 'R' && (!snake.body.get(0).left)) {
				snake.body.get(0).dx = 1;
				snake.body.get(0).dy = 0;
				snake.body.get(0).right = true;
				snake.body.get(0).left = false;
				snake.body.get(0).down = false;
				snake.body.get(0).up = false;
			}
			else if(direction == 'L' && (!snake.body.get(0).right)) {
				snake.body.get(0).dx = -1;
				snake.body.get(0).dy = 0;
				snake.body.get(0).right = false;
				snake.body.get(0).left = true;
				snake.body.get(0).down = false;
				snake.body.get(0).up = false;
			}
			else if(direction == 'D' && (!snake.body.get(0).up)) {
				snake.body.get(0).dx = 0;
				snake.body.get(0).dy = 1;
				snake.body.get(0).right = false;
				snake.body.get(0).left = false;
				snake.body.get(0).down = true;
				snake.body.get(0).up = false;
			}
			else if(direction == 'U' && (!snake.body.get(0).down)) {
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
	
	private void move(Snake snake) 
	{
		for(int i = 0;i < snake.maxLength;i++) {
			snake.body.get(i).x += snake.body.get(i).dx;
			if(snake.body.get(i).x > (weight/grid) - 1) {
				snake.body.get(i).x = 0;
			}
			else if(snake.body.get(i).x < 0) {
				snake.body.get(i).x = (weight/grid) - 1;
			}
			snake.body.get(i).y += snake.body.get(i).dy;
			if(snake.body.get(i).y > (height/grid) - 1) {
				snake.body.get(i).y = 0;
			}
			else if(snake.body.get(i).y < 0) {
				snake.body.get(i).y = (height/grid) - 1;
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
	
	//
	private boolean collisionSA(Snake s, Apple a){
		for(int i = 0;i < s.maxLength;i++) {
			if((a.x==s.body.get(i).x)&&(a.y==s.body.get(i).y)) {
				return true;
			}
		}
		return false;
	}

	private void collisionHA(Snake[] snakes, Apple a) {
		for(int i=0; i<snakes.length; i++)
		{
			Snake s = snakes[i];
			
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
				if(s.maxLength > 10)
				{
					s.updateLevel();
				}
			}
		}
		
		while(true){
			a.x = random.nextInt(49);
			a.y = random.nextInt(49);
			
			for(int i=0; i<snakes.length; i++)
			{
				if(collisionSA(snake[i], apple)){
					continue;
				}
			}
			break;
		}
	}
	
}