import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.BindException;
import java.net.Socket; 

public class GamePlayerDo {
	public static void main(String[] args) throws IOException {
		String server = "192.168.15.1";
		int port = 4999;
		Socket gameSocket = null;
		
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
	Board board;
	private Socket gameSocket = null;
	Snake mySnake;
	int playerNumber;
	
	InputStream ip;
	OutputStream op;
	ObjectOutputStream objectOutputStream;
	Writer charOutputWriter;
	ObjectInputStream objectInputStream;
	
	char inputControl;
	
	ControlInputSender(Socket socket) throws IOException
	{
		gameSocket = socket;
		op = socket.getOutputStream();
		objectOutputStream = new ObjectOutputStream(op);
		ip = socket.getInputStream();
		objectInputStream = new ObjectInputStream(ip);
		
		mySnake = new Snake();
		board = new Board();
		board.init();
		inputControl = 'R';
		
		System.out.println("Game Player Init Complete");
	}
	
	public void run() {
		while(true)
		{
			try {
				objectOutputStream.writeObject(mySnake);
				System.out.println("Writing Snake object Complete");
				objectOutputStream.write((int)inputControl);
				System.out.println("Writing Input Control Complete");
				
				playerNumber = objectInputStream.read();
				System.out.println("Reading Player Number Complete");
				board.snakes = (Snake[])objectInputStream.readObject();
				System.out.println("Reading snake objects Complete");
				board.apple = (Apple)objectInputStream.readObject();
				System.out.println("Reading apple object Complete");
				mySnake = board.snakes[playerNumber];
				
				board.repaint();
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