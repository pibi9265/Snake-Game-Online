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

public class StartWindow implements ActionListener {
    private JFrame startFrame;
    private GameWindow gameWindow;
    private String server = "127.0.0.1";
    private int port = 49152;
    private Socket socket;
    private ClientReader clientReader;

    private JTextArea ipTextArea;
    private JTextArea portTextArea;

    public StartWindow() {
        clientReader = new ClientReader(gameWindow);

        socket = null;

        startFrame = new JFrame();
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(Board.startFrameWidth, Board.startFrameHeight);
        startFrame.setResizable(false);

        JPanel panel = new JPanel();
        startFrame.add(panel);

        JButton button = new JButton("Start");
        panel.add(button);
        button.addActionListener(this);

        ipTextArea = new JTextArea(server);
        ipTextArea.setColumns(Board.ipTextAreaColumns);
        ipTextArea.setRows(Board.ipTextAreaRows);
        panel.add(ipTextArea);

        portTextArea = new JTextArea(Integer.toString(port));
        portTextArea.setColumns(Board.portTextAreaColumns);
        portTextArea.setRows(Board.portTextAreaRows);
        panel.add(portTextArea);

        gameWindow = new GameWindow(this);

        startFrame.setVisible(true);
        startFrame.requestFocus();
    }

    JFrame getFrame(){
        return startFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            server = ipTextArea.getText();
            port = Integer.parseInt(portTextArea.getText());
            socket = new Socket(server, port);
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
