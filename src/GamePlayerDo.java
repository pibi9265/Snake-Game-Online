import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.BindException;
import java.net.Socket; 

public class GamePlayerDo {
	public static void main(String[] args) throws IOException {
		String server = "192.168.58.1";
		int port = 5001;
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
	
	Writer charOutputWriter;
	char inputControl;
	
	ControlInputSender(Socket socket) throws IOException
	{
		gameSocket = socket;
		mySnake = new Snake();
		board = new Board();
		board.init();
		inputControl = 'R';
	}
	
	public void run() {
		while(true)
		{
			try {
				Thread.sleep(50);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(gameSocket.getOutputStream());
				objectOutputStream.writeObject(mySnake);
				
				Writer charOutputWriter = new OutputStreamWriter(gameSocket.getOutputStream());
				charOutputWriter.write(inputControl);
				
				playerNumber = gameSocket.getInputStream().read();
				
				ObjectInputStream objectInputStream = new ObjectInputStream(gameSocket.getInputStream());
				board.snakes = (Snake[])objectInputStream.readObject();

				board.apple = (Apple)objectInputStream.readObject();
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