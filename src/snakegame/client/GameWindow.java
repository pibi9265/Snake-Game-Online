package snakegame.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;

import javax.swing.JFrame;

import snakegame.client.GameComponent;
import snakegame.client.ClientReader;
//import snakegame.client.ClientSender;
import snakegame.element.Board;

public class GameWindow implements KeyListener, WindowListener {
	private JFrame gameFrame;
	private GameComponent gameComponent;

	private JFrame startFrame;

	private ClientReader clientReader;
	//private ClientSender clientSender;

	private int id;

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
		//clientSender = null;

		// id 초기화
		id = -1;
	}

	public void startGame(Socket socket) {
		startFrame.setVisible(false);
		gameFrame.setVisible(true);
		gameFrame.requestFocus();

		clientReader = new ClientReader(socket, this, gameComponent);
		new Thread(clientReader).start();

		//clientSender = new ClientSender(socket);
	}

	public void reset() {
		// Reader 초기화
		clientReader = null;
		// Sender 초기화
		//clientSender.reset();
		//clientSender = null;

		id = -1;

		gameFrame.setVisible(false);
		startFrame.setVisible(true);
		startFrame.requestFocus();
	}

	public JFrame getFrame() {
		return gameFrame;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameComponent.keyPressed) {
			//clientSender.sending('R');
			gameComponent.keyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && !gameComponent.keyPressed) {
			//clientSender.sending('L');
			gameComponent.keyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !gameComponent.keyPressed) {
			//clientSender.sending('D');
			gameComponent.keyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP && !gameComponent.keyPressed) {
			//clientSender.sending('U');
			gameComponent.keyPressed = true;
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
