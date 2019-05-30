package snakegame.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.JFrame;

import snakegame.client.GameComponent;
import snakegame.client.ClientReader;
import snakegame.element.Apple;
import snakegame.element.Board;
import snakegame.element.Snake;
import snakegame.element.SnakeSetDirInterface;

public class GameWindow implements KeyListener, WindowListener, Runnable {
	private JFrame gameFrame;
	private GameComponent gameComponent;
	private JFrame startFrame;

	private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

	private int curPlayer;
	private boolean stop;
	private ArrayList<Snake> snakes;
	private Apple apple;
	private char dir;
	
	private SnakeSetDirInterface ssdi;
	
	public GameWindow(JFrame startFrame) {
		// game 프레임 생성
		gameFrame = new JFrame();
		gameFrame.setSize(Board.width + (Board.grid / 2), Board.height + (Board.grid * 2));
		gameFrame.setResizable(false);
		// 프레임에 key 리스너 연결
		gameFrame.addKeyListener(this);
		// 프레임에 윈도우 리스너 연결
		gameFrame.addWindowListener(this);
		// game component 생성 및 프레임에 추가
		gameComponent = new GameComponent();
		gameFrame.getContentPane().add(gameComponent);

		// start 프레임 지정
		this.startFrame = startFrame;		
		
		curPlayer = -1;
		stop = false;
		snakes = null;
		apple = null;
		
		try {
			ssdi = (SnakeSetDirInterface) Naming.lookup("rmi://" + Board.DEFAULT_ADDRESS + ":" + (Board.DEFAULT_PORT+2) + "/" + Board.serverName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startGame(Socket socket) {
		startFrame.setVisible(false);
		gameFrame.setVisible(true);
		gameFrame.requestFocus();
		
		new Thread(this).start();
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		 while (!stop) {
	            try {
	            	objectOutputStream.writeChar(dir);
	                objectOutputStream.reset();
	                
	                if (objectInputStream != null) {
	                	curPlayer = objectInputStream.readInt();
	                    snakes = (ArrayList<Snake>) objectInputStream.readObject();
	                    apple = (Apple) objectInputStream.readObject();
	                    gameComponent.paintGameComponents(snakes, apple);
	                }
	            } catch (ClassNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	            }
	            gameComponent.keyPressed = false;
	        }
	}
	
	public void reset() {
		curPlayer = -1;
		
		gameFrame.setVisible(false);
		startFrame.setVisible(true);
		startFrame.requestFocus();
	}

	public JFrame getFrame() {
		return gameFrame;
	}

	public void setId(int id) {
		this.curPlayer = id;
	}

	public int getId() {
		return curPlayer;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameComponent.keyPressed) {
				ssdi.setDir(curPlayer, 'R');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT && !gameComponent.keyPressed) {
				ssdi.setDir(curPlayer, 'L');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !gameComponent.keyPressed) {
				ssdi.setDir(curPlayer, 'D');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_UP && !gameComponent.keyPressed) {
				ssdi.setDir(curPlayer, 'U');
				gameComponent.keyPressed = true;
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
	}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {
		//clientReader.threadStop();
	}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
