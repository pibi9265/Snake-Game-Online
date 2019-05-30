package snakegame.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import snakegame.client.GameComponent;
import snakegame.client.ClientReader;
import snakegame.element.Board;
import snakegame.element.SnakeSetDirInterface;

public class GameWindow implements KeyListener, WindowListener {
	private JFrame gameFrame;
	private GameComponent gameComponent;

	private JFrame startFrame;

	private ClientReader clientReader;

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

		// Reader, Sender 초기화
		clientReader = null;

		// ssdi 초기화
		try {
			ssdi = (SnakeSetDirInterface) Naming.lookup("rmi://" + Board.DEFAULT_ADDRESS + ":" + (Board.DEFAULT_PORT+2) + "/" + Board.serverName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	public void startGame(Socket socket) {
		startFrame.setVisible(false);
		gameFrame.setVisible(true);
		gameFrame.requestFocus();
		
		clientReader = new ClientReader(socket, this, gameComponent);
		
		new Thread(clientReader).start();
	}

	public void reset() {
		// Reader 초기화
		clientReader = null;

		gameFrame.setVisible(false);
		startFrame.setVisible(true);
		startFrame.requestFocus();
	}

	public JFrame getFrame() {
		return gameFrame;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameComponent.keyPressed) {
				ssdi.setDir(clientReader.getId(), 'R');
				//clientSender.sending('R');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT && !gameComponent.keyPressed) {
				ssdi.setDir(clientReader.getId(), 'L');
				//clientSender.sending('L');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !gameComponent.keyPressed) {
				ssdi.setDir(clientReader.getId(), 'D');
				//clientSender.sending('D');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_UP && !gameComponent.keyPressed) {
				ssdi.setDir(clientReader.getId(), 'U');
				//clientSender.sending('U');
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
		clientReader.threadStop();
	}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
