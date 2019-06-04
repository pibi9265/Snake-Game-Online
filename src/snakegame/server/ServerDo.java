package snakegame.server;

import snakegame.server.ServerWindow;

public class ServerDo {
    public static void main(String[] args) {
		ServerWindow serverWindow = new ServerWindow();
		serverWindow.getFrame().setVisible(true);
		serverWindow.getFrame().requestFocus();
		serverWindow.start();
	}
}
