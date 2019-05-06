import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer implements Runnable {
	private GameServerRunnable players[] = new ChatServerRunnable[3];
	public int playerCount = 0;
	
	private int ePort = -1;
	
	public GameServer(int port)
	{
		this.ePort = port;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

class GameServerRunnable implements Runnable {
	protected ChatServer chatServer = null;
	protected Socket clientSocket = null;
	protected PrintWriter out = null;
	protected BufferedReader in = null;
	public int clientID = -1;
}
