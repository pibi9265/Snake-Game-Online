package snakegame.element;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import snakegame.server.ServerWindow;

public class SnakeSetDirImpl extends UnicastRemoteObject implements SnakeSetDirInterface{
    private static final long serialVersionUID = 1L;

    public ArrayList<ServerWindow> rooms;

    public SnakeSetDirImpl(ArrayList<ServerWindow> rooms) throws RemoteException {
        super();
        this.rooms = rooms;
    }

    @Override
    synchronized public void setDir(int roomNumber, int id, char dir) {
    	Snake snake = rooms.get(roomNumber).snakes.get(id);
    	
		if(dir == 'R' && (!snake.body.get(0).left)) {
			snake.body.get(0).dx = 1;
			snake.body.get(0).dy = 0;
			snake.body.get(0).right = true;
			snake.body.get(0).left = false;
			snake.body.get(0).down = false;
			snake.body.get(0).up = false;
		}
		else if(dir == 'L' && (!snake.body.get(0).right)) {
			snake.body.get(0).dx = -1;
			snake.body.get(0).dy = 0;
			snake.body.get(0).right = false;
			snake.body.get(0).left = true;
			snake.body.get(0).down = false;
			snake.body.get(0).up = false;
		}
		else if(dir == 'D' && (!snake.body.get(0).up)) {
			snake.body.get(0).dx = 0;
			snake.body.get(0).dy = 1;
			snake.body.get(0).right = false;
			snake.body.get(0).left = false;
			snake.body.get(0).down = true;
			snake.body.get(0).up = false;
		}
		else if(dir == 'U' && (!snake.body.get(0).down)) {
			snake.body.get(0).dx = 0;
			snake.body.get(0).dy = -1;
			snake.body.get(0).right = false;
			snake.body.get(0).left = false;
			snake.body.get(0).down = false;
			snake.body.get(0).up = true;
		}
    }
}
