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

import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;

import snakegame.client.GameComponent;
import snakegame.client.ClientReader;
import snakegame.element.Board;
import snakegame.element.SnakeSetDirInterface;

public class GameWindow implements KeyListener, WindowListener {
	private JFrame gameFrame;
	private GameComponent gameComponent;

	private JFrame startFrame;

	public ClientReader clientReader;
	private SnakeSetDirInterface ssdi;

	public GameWindow(JFrame startFrame) {
		// game �봽�젅�엫 �깮�꽦
		gameFrame = new JFrame();
		gameFrame.setSize(Board.width + (Board.grid / 2), Board.height + (Board.grid * 2));
		gameFrame.setResizable(false);
		// �봽�젅�엫�뿉 key 由ъ뒪�꼫 �뿰寃�
		gameFrame.addKeyListener(this);
		// �봽�젅�엫�뿉 �쐢�룄�슦 由ъ뒪�꼫 �뿰寃�
		gameFrame.addWindowListener(this);
		// game component �깮�꽦 諛� �봽�젅�엫�뿉 異붽�
		gameComponent = new GameComponent();
		gameFrame.getContentPane().add(gameComponent);

		// start �봽�젅�엫 吏��젙
		this.startFrame = startFrame;

		// Reader, Sender 珥덇린�솕
		clientReader = null;
	}

	public void startGame(SSLSocket socket) {
		startFrame.setVisible(false);
		gameFrame.setVisible(true);
		gameFrame.requestFocus();

		try {
			ssdi = (SnakeSetDirInterface) Naming.lookup("rmi://" + socket.getInetAddress().getHostAddress() + ":" + (Board.DEFAULT_PORT + 1) + "/" + Board.serverName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
		clientReader = new ClientReader(socket, this, gameComponent);
		new Thread(clientReader).start();
	}

	public void reset() {
		// Reader 珥덇린�솕
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
			if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameComponent.keyPressed && clientReader.getId() != -1) {
				ssdi.setDir(clientReader.getRoomNumber(), clientReader.getId(), 'R');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT && !gameComponent.keyPressed && clientReader.getId() != -1) {
				ssdi.setDir(clientReader.getRoomNumber(), clientReader.getId(), 'L');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !gameComponent.keyPressed && clientReader.getId() != -1) {
				ssdi.setDir(clientReader.getRoomNumber(), clientReader.getId(), 'D');
				gameComponent.keyPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_UP && !gameComponent.keyPressed && clientReader.getId() != -1) {
				ssdi.setDir(clientReader.getRoomNumber(), clientReader.getId(), 'U');
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
