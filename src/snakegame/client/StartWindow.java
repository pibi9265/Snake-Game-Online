package snakegame.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

import snakegame.element.Board;
import snakegame.client.GameWindow;

public class StartWindow implements ActionListener {
    private JFrame startFrame;
    private JTextArea ipTextArea;
    //private JTextArea portTextArea;
    //private JTextArea playerTextArea;

    private GameWindow gameWindow;

    SSLSocketFactory sslSocketFactory;
    SSLSocket sslSocket;
    private String address = Board.DEFAULT_ADDRESS;
    //private int port = Board.DEFAULT_PORT;
    
    public StartWindow() {
        // start �봽�젅�엫 �깮�꽦
        startFrame = new JFrame();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(Board.startFrameWidth, Board.startFrameHeight);
        startFrame.setResizable(false);
        // panel �깮�꽦
        JPanel panel = new JPanel();
        startFrame.add(panel);
        // start 踰꾪듉 �깮�꽦 & panel�뿉 異붽�
        JButton button = new JButton("Start");
        panel.add(button);
        button.addActionListener(this);
        // address text area �깮�꽦 & panel�뿉 異붽�
        ipTextArea = new JTextArea(address);
        ipTextArea.setColumns(Board.ipTextAreaColumns);
        ipTextArea.setRows(Board.ipTextAreaRows);
        panel.add(ipTextArea);
        /* port text area �깮�꽦 & panel�뿉 異붽�
        portTextArea = new JTextArea(Integer.toString(port));
        portTextArea.setColumns(Board.portTextAreaColumns);
        portTextArea.setRows(Board.portTextAreaRows);
        panel.add(portTextArea);
        */
        /* player text area �깮�꽦 & panel�뿉 異붽�
        playerTextArea = new JTextArea(Integer.toString(2));
        playerTextArea.setColumns(Board.playerTextAreaColums);
        playerTextArea.setRows(Board.playerTextAreaRows);
        panel.add(playerTextArea);
        */

        // game 李� �깮�꽦
        gameWindow = new GameWindow(startFrame);

        // socket
        System.setProperty("javax.net.ssl.trustStore", "trustedcerts");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        sslSocket = null;
        
        // start �봽�젅�엫 湲곕낯 �꽕�젙
        //startFrame.setVisible(true);
        //startFrame.requestFocus();
    }

    public JFrame getFrame(){
        return startFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            address = ipTextArea.getText();
            //port = Integer.parseInt(portTextArea.getText());
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(address, Board.DEFAULT_PORT);
            
            String[] supported = sslSocket.getSupportedCipherSuites();
            sslSocket.setEnabledCipherSuites(supported);
            sslSocket.startHandshake();
            
            gameWindow.startGame(sslSocket);
        } catch (UnknownHostException unknownHostException) {
            unknownHostException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
