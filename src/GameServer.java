
public class GameServer implements Runnable {
	private ChatServerRunnable clients[] = new ChatServerRunnable[3];
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

class ChatServerRunnable implements Runnable {
	protected ChatServer chatServer = null;
	protected Socket clientSocket = null;
	protected PrintWriter out = null;
	protected BufferedReader in = null;
	public int clientID = -1;
}
