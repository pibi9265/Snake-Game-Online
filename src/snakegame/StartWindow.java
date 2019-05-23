package snakegame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.IOException;

import snakegame.Board;
import snakegame.GameWindow;
import snakegame.ClientReader;
import snakegame.ClientSender;

public class StartWindow implements ActionListener {
    private JFrame startFrame;
    private JTextArea ipTextArea;
    private JTextArea portTextArea;
    private JTextArea playerTextArea;

    private GameWindow gameWindow;

    private Socket socket;
    private String address = Board.DEFAULT_ADDRESS;
    private int port = Board.DEFAULT_PORT;

    private ClientReader clientReader;
    public ClientSender clientSender;
    
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
        // port text area 생성 & panel에 추가
        portTextArea = new JTextArea(Integer.toString(port));
        portTextArea.setColumns(Board.portTextAreaColumns);
        portTextArea.setRows(Board.portTextAreaRows);
        panel.add(portTextArea);
        // player text area 생성 & panel에 추가
        playerTextArea = new JTextArea(Integer.toString(2));
        playerTextArea.setColumns(Board.playerTextAreaColums);
        playerTextArea.setRows(Board.playerTextAreaRows);
        panel.add(playerTextArea);

        // game 창 생성
        gameWindow = new GameWindow(this);

        // socket
        socket = null;
        // 기본 address 지정
        address = Board.DEFAULT_ADDRESS;
        // 기본 port 지정
        port = Board.DEFAULT_PORT;

        // Reader, Sender 생성
        clientReader = new ClientReader(gameWindow);
        clientSender = new ClientSender();

        // start 프레임 기본 설정
        startFrame.setVisible(true);
        startFrame.requestFocus();
    }

    JFrame getFrame(){
        return startFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            address = ipTextArea.getText();
            port = Integer.parseInt(portTextArea.getText());
            socket = new Socket(address, port);
            clientSender.setSocket(socket);
            clientReader.setSocket(socket);
            new Thread(clientReader).start();
            startFrame.setVisible(false);
            gameWindow.getFrame().setVisible(true);
            gameWindow.getFrame().requestFocus();
        } catch (UnknownHostException unknownHostException) {
            unknownHostException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
