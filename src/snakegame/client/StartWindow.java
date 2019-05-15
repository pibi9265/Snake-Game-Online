package snakegame.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.net.BindException;
import java.net.UnknownHostException;
import java.io.IOException;

import snakegame.element.Board;
import snakegame.client.GameWindow;
//import snakegame.client.ClientReader;

public class StartWindow implements ActionListener {
    private JFrame startFrame;
    private GameWindow gameWindow;
    private String server = "127.0.0.1";
    private int port = 49152;
    Socket socket;
    //ClientReader clientReader;

    public StartWindow() {
        //clientReader = new ClientReader();

        startFrame = new JFrame();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(Board.startFrameHeight, Board.startFrameWeight);
        startFrame.setResizable(false);

        JPanel panel = new JPanel();
        startFrame.add(panel);

        JButton button = new JButton("Start");
        panel.add(button);
        button.addActionListener(this);

        JTextArea ipTextArea = new JTextArea(server);
        ipTextArea.setColumns(Board.ipTextAreaColumns);
        ipTextArea.setRows(Board.ipTextAreaRows);
        panel.add(ipTextArea);

        JTextArea portTextArea = new JTextArea(Integer.toString(port));
        portTextArea.setColumns(Board.portTextAreaColumns);
        portTextArea.setRows(Board.portTextAreaRows);
        panel.add(portTextArea);

        gameWindow = new GameWindow(this);
    }

    JFrame getFrame(){
        return startFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //while (true) {
            //try {
                //socket = new Socket(server, port);
                ////ClientReader.setSocket(socket);
                ////new Thread(clientReader).start();
                //startFrame.setVisible(false);
                gameWindow.getFrame().setVisible(true);
                gameWindow.getFrame().requestFocus();
                //break;
            //} catch (BindException bindExcption) {
            //    bindExcption.printStackTrace();
            //} catch (UnknownHostException unknownHostException) {
            //    unknownHostException.printStackTrace();
            //} catch (IOException ioException) {
            //    ioException.printStackTrace();
            //}
        //}
    }
}
