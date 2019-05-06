import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.BindException;
import java.net.Socket; 

public class GamePlayerDo {
	public static void main(String[] args) throws IOException {
		String server = "192.168.15.1";
		int port = -1;
		Socket gameSocket = null;
		
		port = 4999;
		
		try {
			gameSocket = new Socket(server, port);
		} catch (BindException b) {
			System.exit(1);
		} catch (IOException i) {
			System.exit(1);
		}
		
		new Thread(new ControlInputSender(gameSocket)).start();
	}
}

class ControlInputSender implements Runnable, KeyListener
{
	Board board = new Board();
	private Socket gameSocket = null;
	Writer charOutputWriter;
	ObjectInputStream objectInputStream;
	Snake[] snake;
	Apple apple;
	char inputControl;
	
	ControlInputSender(Socket socket) throws IOException
	{
		gameSocket = socket;
		charOutputWriter = new OutputStreamWriter(socket.getOutputStream());
		objectInputStream = new ObjectInputStream(socket.getInputStream());
		board.init();
	}
	
	public void run() {
		while(true)
		{
			try {
				Thread.sleep(50);
				charOutputWriter.write(inputControl);
				snake = (Snake[])objectInputStream.readObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			inputControl = 'R';
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			inputControl = 'L';
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			inputControl = 'D';
		}
		else if(e.getKeyCode()==KeyEvent.VK_UP) {
			inputControl = 'U';
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}	
}