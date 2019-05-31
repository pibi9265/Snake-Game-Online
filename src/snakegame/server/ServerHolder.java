package snakegame.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import snakegame.element.Board;

public class ServerHolder extends Thread{
	KeyStore ks;
	KeyManagerFactory kmf;
	SSLContext sc;
	SSLServerSocketFactory sslServerFactory;
	SSLServerSocket sslServerSocket;
	
	Selector selector;
	SelectionKey selectionKey;
	ServerSocketChannel serverSocketChannel;
	ServerWindow serverWindow;
	ArrayList<ServerWindow> roomList;
	public int Rooms = 10;
	
	final String runRoot = "D:/Users/HyukWonLee/Desktop/JAVA_PROJECT_SSL/SNAKE_GAME/bin/";  // root change : your system root
	final String ksName = runRoot + ".keystore/SSLSocketServerKey";
	char keyStorePass[] = "3053919".toCharArray();
	char keyPass[] = "3053919".toCharArray();
	
	void startServer() {
		try {
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), keyStorePass);
			
			kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keyPass);
			
			sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			
			sslServerFactory = sc.getServerSocketFactory();
			sslServerSocket = (SSLServerSocket) sslServerFactory.createServerSocket(Board.DEFAULT_PORT);
			
			roomList = new ArrayList<ServerWindow>();
			 for(int i=0; i<Rooms; i++) { 
				 ServerWindow room = new ServerWindow();
				 roomList.add(room); 
			 }
			 
			 new Thread(this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		SSLSocket player;
		try {
			player = (SSLSocket)sslServerSocket.accept();
			
			Iterator<ServerWindow> iter = roomList.iterator();
			
			while(iter.hasNext()) {
				ServerWindow curServ = iter.next();
				if(curServ.addPlayer(player) != -1)
				{
					if(curServ.isStopped())
					{
						curServ.start();
					}
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
