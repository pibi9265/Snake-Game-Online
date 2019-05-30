package snakegame.client;

import snakegame.client.StartWindow;

public class ClientDo{
    public static void main(String[] args) {
		StartWindow startWindow = new StartWindow();
		startWindow.getFrame().setVisible(true);
		startWindow.getFrame().requestFocus();
	}
}
