import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.BindException;
import java.net.Socket; 
import java.util.ArrayList;

public class GamePlayerDo {
	public static void main(String[] args) throws IOException {
		String server = "121.131.169.41";
		int port = 4999;
		Socket gameSocket = null;
		
		try {
			gameSocket = new Socket(server, port);
			System.out.println("Connection Established");
		} catch (BindException b) {
			System.exit(1);
		} catch (IOException i) 
		{
			System.exit(1);
		}
		
		new Thread(new ControlInputSender(gameSocket)).start();
	}
}

class ControlInputSender implements Runnable
{
	//class implementing JComponent for Graphics
	Board board = new Board();
	private Socket gameSocket = null;
	
	ObjectOutputStream objectOutputStream;
	ObjectInputStream objectInputStream;
	
	ControlInputSender(Socket socket) throws IOException
	{
		gameSocket = socket;
		objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectInputStream = new ObjectInputStream(socket.getInputStream());

		board.init();
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		while(true)
		{
			try {
				ArrayList<Snake> temp;
				objectOutputStream.writeChar(board.inputControl);
				objectOutputStream.flush();
				
				//board.snakes.clear();
				//board.snakes.addAll((ArrayList<Snake>)objectInputStream.readObject());
				temp = (ArrayList<Snake>)objectInputStream.readObject();
				System.out.println(temp.get(0).body.get(0).x);
				
				//board.snakes = (ArrayList<Snake>)objectInputStream.readObject();
				board.apple = (Apple)objectInputStream.readObject();

				board.repaint();
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}