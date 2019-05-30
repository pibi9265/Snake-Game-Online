package snakegame.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
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

    private Socket socket;
    private String address = Board.DEFAULT_ADDRESS;
    //private int port = Board.DEFAULT_PORT;
    
    public StartWindow() {
        // start 프레임 생성
        startFrame = new JFrame();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(Board.startFrameWidth, Board.startFrameHeight);
        startFrame.setResizable(false);
        // panel 생성
        JPanel panel = new JPanel();
        startFrame.add(panel);
        // start 버튼 생성 & panel에 추가
        JButton button = new JButton("Start");
        panel.add(button);
        button.addActionListener(this);
        // address text area 생성 & panel에 추가
        ipTextArea = new JTextArea(address);
        ipTextArea.setColumns(Board.ipTextAreaColumns);
        ipTextArea.setRows(Board.ipTextAreaRows);
        panel.add(ipTextArea);
        /* port text area 생성 & panel에 추가
        portTextArea = new JTextArea(Integer.toString(port));
        portTextArea.setColumns(Board.portTextAreaColumns);
        portTextArea.setRows(Board.portTextAreaRows);
        panel.add(portTextArea);
        */
        /* player text area 생성 & panel에 추가
        playerTextArea = new JTextArea(Integer.toString(2));
        playerTextArea.setColumns(Board.playerTextAreaColums);
        playerTextArea.setRows(Board.playerTextAreaRows);
        panel.add(playerTextArea);
        */

        // game 창 생성
        gameWindow = new GameWindow(startFrame);

        // socket
        socket = null;

        // start 프레임 기본 설정
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
            socket = new Socket(address, Board.DEFAULT_PORT);
            
            gameWindow.startGame(socket);
            socket = null;
        } catch (UnknownHostException unknownHostException) {
            unknownHostException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
