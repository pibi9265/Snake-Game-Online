import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.BindException;
import java.net.Socket; 
import java.util.ArrayList;

public class GamePlayerDo {
	public static void main(String[] args) throws IOException {
		String server = "192.168.15.1";
		int port = 4999;
		Socket gameSocket = null;
		
		try {
			gameSocket = new Socket(server, port);
			System.out.println("Connection Established");
		} catch (BindException b) {
			System.exit(1);
		} catch (IOException i) {
			System.exit(1);
		}
		
		new Thread(new ControlInputSender(gameSocket)).start();
	}
}

class ControlInputSender implements Runnable
{
	Board board = new Board();
	private Socket gameSocket = null;
	
	ObjectOutputStream objectOutputStream;
	ObjectInputStream objectInputStream;
	Writer charWriter;
	
	ControlInputSender(Socket socket) throws IOException
	{
		gameSocket = socket;
		objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectInputStream = new ObjectInputStream(socket.getInputStream());

		board.init();
	}
	
	public void run() {
		while(true)
		{
			try {
				objectOutputStream.writeChar(board.inputControl);
				
				board.snakes.clear();
				board.snakes.addAll((ArrayList<Snake>)objectInputStream.readObject());
				board.apple = (Apple)objectInputStream.readObject();
				System.out.println(board.snakes.get(0).body.get(0).x);
				System.out.println(board.apple.x);
				
				board.repaint();
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}