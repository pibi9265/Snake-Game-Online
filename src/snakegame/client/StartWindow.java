package snakegame.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import snakegame.element.Board;
import snakegame.client.GameWindow;

public class StartWindow implements ActionListener {
    private JFrame startFrame;
    private JTextArea addressTextArea;

    private GameWindow gameWindow;

    public StartWindow() {
        // property 설정
        System.setProperty("javax.net.ssl.trustStore", "clientkeystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

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
        addressTextArea = new JTextArea(Board.DEFAULT_ADDRESS);
        addressTextArea.setColumns(Board.ipTextAreaColumns);
        addressTextArea.setRows(Board.ipTextAreaRows);
        panel.add(addressTextArea);

        // game 창 생성
        gameWindow = new GameWindow(startFrame);

        // start 프레임 기본 설정 (ClientDo로 옮김)
        //startFrame.setVisible(true);
        //startFrame.requestFocus();
    }

    public JFrame getFrame(){
        return startFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        gameWindow.startGame(addressTextArea.getText());
    }
}
