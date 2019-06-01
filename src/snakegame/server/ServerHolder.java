package snakegame.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.Naming;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import snakegame.element.Board;
import snakegame.element.SnakeSetDirImpl;
import snakegame.element.SnakeSetDirInterface;

public class ServerHolder extends Thread{
	private JFrame serverFrame;
	
	KeyStore ks;
	KeyManagerFactory kmf;
	SSLContext sc;
	SSLServerSocketFactory sslServerFactory;
	SSLServerSocket sslServerSocket;
	
	Selector selector;
	SelectionKey selectionKey;
	ArrayList<ServerWindow> roomList;
	public int Rooms = 10;
	
	final String runRoot = "D:/이혁원/Desktop/JAVA_PROJECT/Snake_Game/bin/";  // root change : your system root
	final String ksName = runRoot + ".keystore/SSLSocketServerKey";
	char keyStorePass[] = "3053919".toCharArray();
	char keyPass[] = "3053919".toCharArray();
	private SnakeSetDirInterface ssdi;
	
	void startServer() {
		try {
			serverFrame = new JFrame();
			serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			serverFrame.setSize(Board.serverFrameWidth, Board.serverFrameHeight);
			serverFrame.setResizable(false);
			// panel �깮�꽦
			JPanel panel = new JPanel();
			serverFrame.add(panel);
			// address text area �깮�꽦 & panel�뿉 異붽�
			String addresString = new String("Hello, Snakes! [" + InetAddress.getLocalHost().getHostAddress() + "]");
			JLabel label = new JLabel(addresString);
			panel.add(label);
			
			serverFrame.setVisible(true);
			serverFrame.requestFocus();
			
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
				 roomList.add(new ServerWindow(i)); 
			 }
			
			ssdi = new SnakeSetDirImpl(roomList);
			try{
				Naming.rebind("rmi://" + InetAddress.getLocalHost().getHostAddress() + ":" + (Board.DEFAULT_PORT + 1) + "/" + Board.serverName, ssdi);
			} catch(IOException e){
				e.printStackTrace();
				label.setText("RMI Error");
			}
				
			 new Thread(this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run()
	{
		try {
			SSLSocket player = (SSLSocket)sslServerSocket.accept();
			
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
			
			/*
			ServerWindow server = new ServerWindow();
			server.addPlayer(player);
			server.start();
			*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
