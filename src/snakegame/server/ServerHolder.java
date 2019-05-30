package snakegame.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
//import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import snakegame.element.Board;

public class ServerHolder {
	Selector selector;
	SelectionKey selectionKey;
	ServerSocketChannel serverSocketChannel;
	ServerWindow serverWindow;
	// ArrayList<ServerWindow> roomList;
	// public int Rooms = 10;

	void startServer() {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			ServerSocket socket = serverSocketChannel.socket();
			socket.bind(new InetSocketAddress(Board.DEFAULT_PORT));
			serverSocketChannel.configureBlocking(false);
			selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			serverWindow = new ServerWindow();
			// roomList = new ArrayList<ServerWindow>();

			/*
			 * for(int i=0; i<Rooms; i++) { ServerWindow room = new ServerWindow();
			 * roomList.add(room); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					try {
						int keyCount = selector.select();

						if (keyCount == 0) {
							continue;
						}

						Set<SelectionKey> selectedKeys = selector.selectedKeys();
						Iterator<SelectionKey> iterator = selectedKeys.iterator();

						while (iterator.hasNext()) {
							SelectionKey selectionKey = iterator.next();

							if (selectionKey.isAcceptable()) {
								accept(selectionKey);
							}
							iterator.remove();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	void accept(SelectionKey selectionKey) {
		SocketChannel player;

		try {
			player = serverSocketChannel.accept();
			serverWindow.addPlayer(player.socket());
			if(serverWindow.isStopped()){
				serverWindow.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
    	try {
			player = serverSocketChannel.accept();
	    	Iterator<ServerWindow> iter = roomList.iterator();
	    	
	    	while(iter.hasNext()) {
	    		ServerWindow curServ = iter.next();
	    		if(curServ.addPlayer(player.socket()) != -1)
	    		{
	    			if(curServ.isStopped())
	    			{
	    				curServ.start();
	    			}
	    			return;
	    		}
	    	}
	    //모든 방이 꽉 찼음을 플레이어에게 알리기
    	} catch (Exception e){
			e.printStackTrace();
		}
		*/
    }
}
