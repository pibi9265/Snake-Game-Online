package snakegame.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;

import snakegame.client.GameComponent;
import snakegame.element.Board;
import snakegame.element.SnakeControllerInterface;
import snakegame.rmisslsocketfactory.RMISSLClientSocketFactory;

public class GameWindow implements KeyListener, WindowListener {
	private JFrame gameFrame;
	private GameComponent gameComponent;

	private JFrame startFrame;

	private Registry registry;
	private SnakeControllerInterface snakeController;

	private int index;

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

		// snakeController 초기화
		registry = null;
		snakeController = null;

		// id 초기화
		index = -1;
	}

	public void startGame(String address) {
		startFrame.setVisible(false);
		gameFrame.setVisible(true);
		gameFrame.requestFocus();

		// snakeController 생성
		try {
			registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostName(), Board.DEFAULT_PORT, new RMISSLClientSocketFactory());
			snakeController = (SnakeControllerInterface) registry.lookup(Board.snakeControllerName);
		} catch (Exception e) {
			e.printStackTrace();
			reset();
        }
	}

	public void reset() {
		gameComponent.reset();

		registry = null;
		snakeController = null;

		gameFrame.setVisible(false);
		startFrame.setVisible(true);
		startFrame.requestFocus();
	}

	public JFrame getFrame() {
		return gameFrame;
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		try {
			if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT && !gameComponent.keyPressed && index != -1) {
				snakeController.setDir(index, 'R');
				gameComponent.keyPressed = true;
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT && !gameComponent.keyPressed && index != -1) {
				snakeController.setDir(index, 'L');
				gameComponent.keyPressed = true;
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN && !gameComponent.keyPressed && index != -1) {
				snakeController.setDir(index, 'D');
				gameComponent.keyPressed = true;
			} else if (keyEvent.getKeyCode() == KeyEvent.VK_UP && !gameComponent.keyPressed && index != -1) {
				snakeController.setDir(index, 'U');
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
		reset();
	}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
