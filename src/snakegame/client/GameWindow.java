package snakegame.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import snakegame.client.GameComponent;
import snakegame.element.Board;
import snakegame.element.SnakeControllerInterface;
import snakegame.rmisslsocketfactory.RMISSLClientSocketFactory;

public class GameWindow implements Runnable, KeyListener, WindowListener {
	private JFrame gameFrame;
	private GameComponent gameComponent;

	private JFrame startFrame;

	private Registry registry;
	private SnakeControllerInterface snakeController;

	private int id;

	private boolean stop;

	public GameWindow(JFrame startFrame) {
		// game 프레임 생성
		gameFrame = new JFrame();
		gameFrame.setSize(Board.width + (Board.grid / 2) + (Board.grid * 10), Board.height + (Board.grid * 2));
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

		// snakeController 초기화
		registry = null;
		snakeController = null;

		// id 초기화
		id = -1;

		// stop 초기화
		stop = false;
	}

	public void startGame(String address, String playerName) {
		startFrame.setVisible(false);
		gameFrame.setVisible(true);
		gameFrame.requestFocus();

		// snakeController 생성
		try {
			registry = LocateRegistry.getRegistry(address, Board.DEFAULT_PORT, new RMISSLClientSocketFactory());
			snakeController = (SnakeControllerInterface) registry.lookup(Board.snakeControllerName);
			id = snakeController.addPlayer(playerName);
		} catch (Exception e) {
			e.printStackTrace();
			reset();
		}

		new Thread(this).start();
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				Thread.sleep(Board.sleepTime / 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				gameComponent.paintGameComponents(id, snakeController.getSnakes(), snakeController.getApple(), snakeController.getPlayerNames());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void threadStop() {
		stop = true;
	}

	public void reset() {
		registry = null;
		snakeController = null;
		id = -1;
		stop = false;
	}

	public JFrame getFrame() {
		return gameFrame;
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		try {
			if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT && !gameComponent.keyPressed && id != -1) {
				snakeController.setDir(id, 'R');
				gameComponent.keyPressed = true;
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT && !gameComponent.keyPressed && id != -1) {
				snakeController.setDir(id, 'L');
				gameComponent.keyPressed = true;
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN && !gameComponent.keyPressed && id != -1) {
				snakeController.setDir(id, 'D');
				gameComponent.keyPressed = true;
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_UP && !gameComponent.keyPressed && id != -1) {
				snakeController.setDir(id, 'U');
				gameComponent.keyPressed = true;
			}
		} catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		threadStop();
		try {
			Thread.sleep(Board.sleepTime / 10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			snakeController.removePlayer(id);
		} catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}

		gameComponent.reset();
		reset();

		gameFrame.setVisible(false);
		startFrame.setVisible(true);
		startFrame.requestFocus();
	}
	public void windowClosed(WindowEvent e) {
		threadStop();
		try {
			Thread.sleep(Board.sleepTime / 10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		try {
			snakeController.removePlayer(id);
		} catch (RemoteException remoteException) {
			remoteException.printStackTrace();
		}

		gameComponent.reset();
		reset();

		gameFrame.setVisible(false);
		startFrame.setVisible(true);
		startFrame.requestFocus();
	}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
